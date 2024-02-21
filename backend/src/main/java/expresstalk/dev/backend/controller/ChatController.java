package expresstalk.dev.backend.controller;

import expresstalk.dev.backend.dto.*;
import expresstalk.dev.backend.entity.GroupChat;
import expresstalk.dev.backend.entity.PrivateChat;
import expresstalk.dev.backend.entity.PrivateChatMessage;
import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.enums.UserStatus;
import expresstalk.dev.backend.service.ChatService;
import expresstalk.dev.backend.service.SessionService;
import expresstalk.dev.backend.service.UserService;
import expresstalk.dev.backend.utils.Converter;
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

import java.util.UUID;

@RestController
@RequestMapping("/chats")
public class ChatController {
    private final UserService userService;
    private final ChatService chatService;
    private final SessionService sessionService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public ChatController(UserService userService, ChatService chatService, SessionService sessionService, SimpMessagingTemplate simpMessagingTemplate) {
        this.userService = userService;
        this.chatService = chatService;
        this.sessionService = sessionService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/{chatStrId}")
    private void sendPrivateChatMessage(
            @DestinationVariable String chatStrId,
            @Payload SendPrivateChatMessageDto sendPrivateChatMessageDto,
            Message<?> message
    ) {
        try {
            MessageHeaders headers = message.getHeaders();
            HttpServletRequest request = (HttpServletRequest)SimpMessageHeaderAccessor.getSessionAttributes(headers).get("request");
            sessionService.ensureSessionExistense(request);

            UUID chatId = UUID.randomUUID();
            try {
                chatId = Converter.convertStringToUUID(chatStrId);
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provided chat id in the path is not UUID");
            }

            ValidationErrorChecker.<SendPrivateChatMessageDto>checkDtoForErrors(sendPrivateChatMessageDto);

            UUID userId = sessionService.getUserIdFromSession(request);
            User sender = userService.findById(userId);
            User receiver = chatService.getSecondUserOfPrivateChat(sender.getId(), chatId);

            chatService.ensureUserPermissionToSendMessageInPrivateChat(sender, chatId);

            PrivateChatMessage privateChatMessage = chatService.saveMessage(sendPrivateChatMessageDto, sender.getId(), receiver.getId());
            ClientPrivateChatMessageDto clientPrivateChatMessageDto = new ClientPrivateChatMessageDto(
                    sender.getLogin(),
                    privateChatMessage.getContent(),
                    privateChatMessage.getCreatedAt()
            );
            LastMessageDto lastMessageDto = new LastMessageDto(
                    chatId,
                    privateChatMessage.getContent()
            );

            simpMessagingTemplate.convertAndSend("/chats/" + receiver.getId(), lastMessageDto);
            simpMessagingTemplate.convertAndSend("/private_chat/" + chatStrId, clientPrivateChatMessageDto);
        } catch (Exception ex) {
            simpMessagingTemplate.convertAndSend("/private_chat/" + chatStrId + "/errors", ex.getMessage());
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

        UUID chatId = UUID.randomUUID();
        try {
            chatId = Converter.convertStringToUUID(chatStrId);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provided chat id in the path is not UUID");
        }

        PrivateChat chat = chatService.getPrivateChat(chatId);

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
        PrivateChat chat = chatService.createPrivateChat(userId, secondMemberId);

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
        GroupChat chat = chatService.createGroupChat(userId, createGroupChatRoomDto.groupName());

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
        UUID chatId = UUID.randomUUID();
        try {
            chatId = Converter.convertStringToUUID(chatStrId);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provided chat id in the path is not UUID");
        }
        UUID userId = sessionService.getUserIdFromSession(request);
        User user = userService.findById(userId);
        GroupChat chat = chatService.getGroupChat(user, chatId);

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
        GroupChat groupChat = chatService.findGroupChatById(chatId);

        chatService.addMemberToGroupChat(groupChat, admin, member);
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
        GroupChat groupChat = chatService.findGroupChatById(chatId);

        chatService.removeMemberFromGroupChat(groupChat, admin, member);
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
        GroupChat groupChat = chatService.findGroupChatById(chatId);
        User admin = userService.findById(userId);
        User member = userService.findById(memberId);

        chatService.setRoleInGroupChat(groupChat, admin, member, setUserRoleInGroupChatDto.groupChatRole());
    }
}
