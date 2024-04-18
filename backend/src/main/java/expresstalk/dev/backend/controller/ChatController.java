package expresstalk.dev.backend.controller;

import expresstalk.dev.backend.dto.*;
import expresstalk.dev.backend.entity.*;
import expresstalk.dev.backend.enums.UserStatus;
import expresstalk.dev.backend.service.*;
import expresstalk.dev.backend.utils.ValidationErrorChecker;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/chats")
public class ChatController {
    private final UserService userService;
    private final PrivateChatService privateChatService;
    private final GroupChatSerivce groupChatSerivce;
    private final ChatService chatService;
    private final SessionService sessionService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public ChatController(UserService userService, PrivateChatService privateChatService,
                          SessionService sessionService, SimpMessagingTemplate simpMessagingTemplate,
                          GroupChatSerivce groupChatSerivce, ChatService chatService) {
        this.userService = userService;
        this.privateChatService = privateChatService;
        this.groupChatSerivce = groupChatSerivce;
        this.chatService = chatService;
        this.sessionService = sessionService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/private/{chatStrId}")
    private void sendPrivateChatMessage(
            @DestinationVariable String chatStrId,
            @Payload SendChatMessageDto sendChatMessageDto,
            Message<?> message
    ) {
        try {
            MessageHeaders headers = message.getHeaders();
            HttpSession session = (HttpSession) SimpMessageHeaderAccessor.getSessionAttributes(headers).get("session");
            sessionService.ensureSessionExistense(session);

            ValidationErrorChecker.<SendChatMessageDto>checkDtoForErrors(sendChatMessageDto);

            UUID chatId = chatService.checkAndGetChatUUID(chatStrId);
            UUID userId = sessionService.getUserIdFromSession(session);
            User sender = userService.findById(userId);

            privateChatService.ensureUserPermissionToSendMessageInChat(sender, chatId);

            User receiver = privateChatService.getSecondUserOfChat(sender.getId(), chatId);

            PrivateChatMessage privateChatMessage = privateChatService.saveMessage(sendChatMessageDto, sender.getId(), receiver.getId());
            ClientPrivateChatMessageDto clientPrivateChatMessageDto = new ClientPrivateChatMessageDto(
                    sender.getLogin(),
                    privateChatMessage.getContent(),
                    privateChatMessage.getCreatedAt()
            );
            LastMessageDto lastMessageDto = new LastMessageDto(chatId,privateChatMessage.getContent());

            simpMessagingTemplate.convertAndSend("/chats/" + receiver.getId(), lastMessageDto);
            simpMessagingTemplate.convertAndSend("/private_chat/" + chatStrId, clientPrivateChatMessageDto);
        } catch (Exception ex) {
            simpMessagingTemplate.convertAndSend("/private_chat/" + chatStrId + "/errors", ex.getMessage());
        }
    }

    @MessageMapping("/group/{chatStrId}")
    private void sendGroupChatMessage(
            @DestinationVariable String chatStrId,
            @Payload SendChatMessageDto sendChatMessageDto,
            Message<?> message
    ) {
        try {
            MessageHeaders headers = message.getHeaders();
            HttpSession session = (HttpSession) SimpMessageHeaderAccessor.getSessionAttributes(headers).get("session");
            sessionService.ensureSessionExistense(session);

            ValidationErrorChecker.<SendChatMessageDto>checkDtoForErrors(sendChatMessageDto);

            UUID chatId = chatService.checkAndGetChatUUID(chatStrId);
            UUID userId = sessionService.getUserIdFromSession(session);
            User sender = userService.findById(userId);
            groupChatSerivce.ensureUserPermissionToSendMessageInChat(sender, chatId);

            List<User> receivers = groupChatSerivce.getOtherUsersOfChat(sender.getId(), chatId);
            GroupChatMessage groupChatMessage = groupChatSerivce.saveMessage(sendChatMessageDto, sender.getId());

            for(User receiver : receivers) {
                LastMessageDto lastMessageDto = new LastMessageDto(chatId,groupChatMessage.getContent());

                simpMessagingTemplate.convertAndSend("/chats/" + receiver.getId(), lastMessageDto);
            }

            ClientPrivateChatMessageDto clientPrivateChatMessageDto = new ClientPrivateChatMessageDto(
                    sender.getLogin(),
                    groupChatMessage.getContent(),
                    groupChatMessage.getCreatedAt()
            );
            simpMessagingTemplate.convertAndSend("/group_chat/" + chatStrId, clientPrivateChatMessageDto);
        } catch (Exception ex) {
            simpMessagingTemplate.convertAndSend("/group_chat/" + chatStrId + "/errors", ex.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "User is not authenticated"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @ResponseBody
    public GetUserChatsDto getChatsPage(HttpServletRequest request) {
        request.getSession(false);
        UUID userId = sessionService.getUserIdFromSession(request);

        try {
            userService.handleStatusTo(userId, UserStatus.ONLINE);
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }

        return chatService.getChats(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/log-out")
    public void deleteSession(HttpSession session) {
        session.invalidate();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Provided chat id in the path is not UUID"),
            @ApiResponse(responseCode = "403", description = "User is not authenticated"),
            @ApiResponse(responseCode = "404", description = "Chat with provided id doesn't exist."),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/private/{chatStrId}")
    @ResponseBody
    public PrivateChat getPrivateChatRoom(@PathVariable String chatStrId, HttpServletRequest request) {
        sessionService.ensureSessionExistense(request);

        UUID chatId = chatService.checkAndGetChatUUID(chatStrId);
        PrivateChat chat = privateChatService.getChat(chatId);

        return chat;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Second user's id must be a type of UUID"),
            @ApiResponse(responseCode = "404", description = "User with provided id doesn't exist"),
            @ApiResponse(responseCode = "403", description = "User is not authenticated"),
            @ApiResponse(responseCode = "409", description = "Private chat with provided two members had already been created"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/private")
    @ResponseBody
    public PrivateChat createPrivateChatRoom(@RequestBody @Valid CreatePrivateChatRoomDto createPrivateChatRoomDto, HttpServletRequest request) {
        UUID userId = sessionService.getUserIdFromSession(request);
        UUID secondMemberId = UUID.fromString(createPrivateChatRoomDto.secondMemberId());
        PrivateChat chat = privateChatService.createPrivateChat(userId, secondMemberId);

        return chat;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "User is not authenticated"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/group")
    @ResponseBody
    public GroupChat createGroupChatRoom(@RequestBody CreateGroupChatRoomDto createGroupChatRoomDto, HttpServletRequest request) {
        UUID userId = sessionService.getUserIdFromSession(request);
        GroupChat chat = groupChatSerivce.createChat(userId, createGroupChatRoomDto.groupName());

        return chat;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Provided chat id in the path is not UUID"),
            @ApiResponse(responseCode = "401", description = "The member is not present in the chat"),
            @ApiResponse(responseCode = "403", description = "User is not authenticated"),
            @ApiResponse(responseCode = "404", description = "Group chat with provided id wasn't found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping ("/group/{chatStrId}")
    @ResponseBody
    public GroupChat getGroupChatRoom(@PathVariable String chatStrId, HttpServletRequest request) {
        UUID chatId = chatService.checkAndGetChatUUID(chatStrId);
        UUID userId = sessionService.getUserIdFromSession(request);
        User user = userService.findById(userId);
        GroupChat chat = groupChatSerivce.getChat(user, chatId);

        return chat;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Chat id must be a type of UUID"),
            @ApiResponse(responseCode = "400", description = "Member's id must be a type of UUID"),
            @ApiResponse(responseCode = "401", description = "User can not add new members to chat because user is not admin"),
            @ApiResponse(responseCode = "403", description = "User is not authenticated"),
            @ApiResponse(responseCode = "404", description = "Chat with provided id wasn't found"),
            @ApiResponse(responseCode = "409", description = "The member is already in this chat"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/group/add")
    public void addUserToGroupChat(
            @RequestBody @Valid AddUserToGroupChatDto addUserToGroupChatDto,
            HttpServletRequest request
    ) {
        UUID userId = sessionService.getUserIdFromSession(request);
        UUID memberId = UUID.fromString(addUserToGroupChatDto.memberId());
        UUID chatId = UUID.fromString(addUserToGroupChatDto.chatId());
        User admin = userService.findById(userId);
        User member = userService.findById(memberId);
        GroupChat groupChat = groupChatSerivce.getChat(chatId);

        groupChatSerivce.addMemberToChat(groupChat, admin, member);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Chat id must be a type of UUID"),
            @ApiResponse(responseCode = "400", description = "Member's id must be a type of UUID"),
            @ApiResponse(responseCode = "400", description = "The member is not present in the chat"),
            @ApiResponse(responseCode = "401", description = "User can not remove members from the chat because user is not admin"),
            @ApiResponse(responseCode = "403", description = "User is not authenticated"),
            @ApiResponse(responseCode = "404", description = "Chat with provided id wasn't found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/group/remove")
    public void removeUserFromGroupChat(
            @RequestBody @Valid RemoveUserFromGroupChatDto removeUserFromGroupChatDto,
            HttpServletRequest request
    ) {
        UUID userId = sessionService.getUserIdFromSession(request);
        UUID memberId = UUID.fromString(removeUserFromGroupChatDto.memberId());
        UUID chatId = UUID.fromString(removeUserFromGroupChatDto.chatId());
        User admin = userService.findById(userId);
        User member = userService.findById(memberId);
        GroupChat groupChat = groupChatSerivce.getChat(chatId);

        groupChatSerivce.removeMember(groupChat, admin, member);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Chat id must be a type of UUID"),
            @ApiResponse(responseCode = "400", description = "Member's id must be a type of UUID"),
            @ApiResponse(responseCode = "400", description = "The member is not present in the chat"),
            @ApiResponse(responseCode = "400", description = "Can not give admin role to the member. The member is already admin in the chat"),
            @ApiResponse(responseCode = "401", description = "User can not set role for members from the chat because user is not admin"),
            @ApiResponse(responseCode = "403", description = "User is not authenticated"),
            @ApiResponse(responseCode = "403", description = "Admin can not take off admin rights from another admin"),
            @ApiResponse(responseCode = "404", description = "Chat with provided id wasn't found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/group/roles")
    public void setUserRoleInGroupChat(
            @RequestBody @Valid SetUserRoleInGroupChatDto setUserRoleInGroupChatDto,
            HttpServletRequest request
    ) {
        UUID userId = sessionService.getUserIdFromSession(request);
        UUID memberId = UUID.fromString(setUserRoleInGroupChatDto.userToGiveRoleId());
        UUID chatId = UUID.fromString(setUserRoleInGroupChatDto.chatId());
        GroupChat groupChat = groupChatSerivce.getChat(chatId);
        User admin = userService.findById(userId);
        User member = userService.findById(memberId);

        groupChatSerivce.setRole(groupChat, admin, member, setUserRoleInGroupChatDto.groupChatRole());
    }
}
