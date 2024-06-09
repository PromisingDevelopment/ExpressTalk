package expresstalk.dev.backend.controller;

import expresstalk.dev.backend.dto.*;
import expresstalk.dev.backend.entity.GroupChat;
import expresstalk.dev.backend.entity.GroupChatMessage;
import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.service.*;
import expresstalk.dev.backend.utils.Converter;
import expresstalk.dev.backend.utils.ValidationErrorChecker;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/group_chats")
public class GroupChatController {
    private final UserService userService;
    private final GroupChatSerivce groupChatSerivce;
    private final ChatService chatService;
    private final SessionService sessionService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public GroupChatController(UserService userService, SessionService sessionService,
                               SimpMessagingTemplate simpMessagingTemplate,
                          GroupChatSerivce groupChatSerivce, ChatService chatService) {
        this.userService = userService;
        this.groupChatSerivce = groupChatSerivce;
        this.chatService = chatService;
        this.sessionService = sessionService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/group_chat/send_message")
    private void sendGroupChatMessage(
            @Payload SendChatMessageDto sendChatMessageDto,
            Message<?> message
    ) {
        try {
            MessageHeaders headers = message.getHeaders();
            HttpSession session = (HttpSession) SimpMessageHeaderAccessor.getSessionAttributes(headers).get("session");
            sessionService.ensureSessionExistense(session);

            ValidationErrorChecker.<SendChatMessageDto>checkDtoForErrors(sendChatMessageDto);

            UUID chatId = chatService.checkAndGetChatUUID(sendChatMessageDto.chatId());
            UUID userId = sessionService.getUserIdFromSession(session);
            User sender = userService.findById(userId);
            groupChatSerivce.ensureUserPermissionToSendMessageInChat(sender, chatId);

            List<User> receivers = groupChatSerivce.getOtherUsersOfChat(sender.getId(), chatId);
            GroupChatMessage groupChatMessage = groupChatSerivce.saveMessage(sendChatMessageDto, sender.getId());

            for(User receiver : receivers) {
                LastMessageDto lastMessageDto = new LastMessageDto(chatId,groupChatMessage.getContent());

                simpMessagingTemplate.convertAndSend("/chats/last_messages/" + receiver.getId(), lastMessageDto);
            }

            ClientChatMessageDto clientChatMessageDto = new ClientChatMessageDto(
                    sender.getId(),
                    sender.getLogin(),
                    groupChatMessage.getContent(),
                    groupChatMessage.getCreatedAt()
            );
            simpMessagingTemplate.convertAndSend("/group_chat/messages/" + sendChatMessageDto.chatId(), clientChatMessageDto);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            simpMessagingTemplate.convertAndSend("/group_chat/messages/" + sendChatMessageDto.chatId() + "/errors", ex.getMessage());
        }
    }

    @MessageMapping("group_chat/add")
    private void addMemberToGroupChat(
            @Payload AddUserToGroupChatDto addUserToGroupChatDto,
            Message<?> message
    ) {
        try {
            MessageHeaders headers = message.getHeaders();
            HttpSession session = (HttpSession) SimpMessageHeaderAccessor.getSessionAttributes(headers).get("session");
            sessionService.ensureSessionExistense(session);

            ValidationErrorChecker.<AddUserToGroupChatDto>checkDtoForErrors(addUserToGroupChatDto);

            UUID chatId = chatService.checkAndGetChatUUID(addUserToGroupChatDto.chatId());
            UUID userId = sessionService.getUserIdFromSession(session);
            User admin = userService.findById(userId);
            User member = userService.findById(Converter.convertStringToUUID(addUserToGroupChatDto.memberId()));
            GroupChat groupChat = groupChatSerivce.getChat(chatId);

            groupChatSerivce.addMemberToChat(groupChat, admin, member);

            String addMemberMessage = admin.getLogin() + " has added " + member.getLogin();

            List<User> receivers = groupChatSerivce.getOtherUsersOfChat(admin.getId(), chatId);
            for(User receiver : receivers) {
                LastMessageDto lastMessageDto = new LastMessageDto(chatId,addMemberMessage);

                simpMessagingTemplate.convertAndSend("/chats/last_messages/" + receiver.getId(), lastMessageDto);
            }

            AnonymousClientChatMessageDto anonChatMessageDto = new AnonymousClientChatMessageDto(addMemberMessage,new Date());
            UpdatedMembersDto updatedMembersDto = new UpdatedMembersDto(chatId, groupChat.getMembers());

            simpMessagingTemplate.convertAndSend("/group_chat/updated_members/" + addUserToGroupChatDto.chatId(), updatedMembersDto);
            simpMessagingTemplate.convertAndSend("/group_chat/anon_messages/" + addUserToGroupChatDto.chatId(), anonChatMessageDto);
        } catch (Exception ex) {
            simpMessagingTemplate.convertAndSend("/group_chat/add/" + addUserToGroupChatDto.chatId() + "/errors", ex.getMessage());
        }
    }

    @MessageMapping("group_chat/remove")
    private void removeMemberFromGroupChat(
            @Payload RemoveUserFromGroupChatDto removeUserFromGroupChatDto,
            Message<?> message
    ) {
        try {
            MessageHeaders headers = message.getHeaders();
            HttpSession session = (HttpSession) SimpMessageHeaderAccessor.getSessionAttributes(headers).get("session");
            sessionService.ensureSessionExistense(session);

            ValidationErrorChecker.<RemoveUserFromGroupChatDto>checkDtoForErrors(removeUserFromGroupChatDto);

            UUID chatId = chatService.checkAndGetChatUUID(removeUserFromGroupChatDto.chatId());
            UUID userId = sessionService.getUserIdFromSession(session);
            User admin = userService.findById(userId);
            User member = userService.findById(Converter.convertStringToUUID(removeUserFromGroupChatDto.memberId()));
            GroupChat groupChat = groupChatSerivce.getChat(chatId);

            groupChatSerivce.removeMemberFromChat(groupChat, admin, member);

            String addMemberMessage = admin.getLogin() + " has removed " + member.getLogin();

            List<User> receivers = groupChatSerivce.getOtherUsersOfChat(admin.getId(), chatId);
            for(User receiver : receivers) {
                LastMessageDto lastMessageDto = new LastMessageDto(chatId,addMemberMessage);

                simpMessagingTemplate.convertAndSend("/chats/last_messages/" + receiver.getId(), lastMessageDto);
            }

            AnonymousClientChatMessageDto anonChatMessageDto = new AnonymousClientChatMessageDto(addMemberMessage,new Date());
            UpdatedMembersDto updatedMembersDto = new UpdatedMembersDto(chatId, groupChat.getMembers());

            simpMessagingTemplate.convertAndSend("/group_chat/updated_members/" + removeUserFromGroupChatDto.chatId(), updatedMembersDto);
            simpMessagingTemplate.convertAndSend("/group_chat/anon_messages/" + removeUserFromGroupChatDto.chatId(), anonChatMessageDto);
        } catch (Exception ex) {
            simpMessagingTemplate.convertAndSend("/group_chat/remove/" + removeUserFromGroupChatDto.chatId() + "/errors", ex.getMessage());
        }
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
    @GetMapping ("/{chatStrId}")
    @ResponseBody
    public GroupChat getGroupChat(@PathVariable String chatStrId, HttpServletRequest request) {
        UUID chatId = chatService.checkAndGetChatUUID(chatStrId);
        UUID userId = sessionService.getUserIdFromSession(request);
        User user = userService.findById(userId);

        return groupChatSerivce.getChat(user, chatId);
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
    @PostMapping("/roles")
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
