package expresstalk.dev.backend.controller;

import expresstalk.dev.backend.dto.request.AddUserToGroupChatDto;
import expresstalk.dev.backend.dto.request.RemoveUserFromGroupChatDto;
import expresstalk.dev.backend.dto.request.SendChatMessageDto;
import expresstalk.dev.backend.dto.request.SetUserRoleInGroupChatDto;
import expresstalk.dev.backend.dto.response.GroupChatMessageDto;
import expresstalk.dev.backend.dto.response.LastMessageDto;
import expresstalk.dev.backend.dto.response.UpdatedMembersDto;
import expresstalk.dev.backend.entity.GroupChat;
import expresstalk.dev.backend.entity.GroupMessage;
import expresstalk.dev.backend.entity.SystemMessage;
import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.service.ChatService;
import expresstalk.dev.backend.service.GroupChatService;
import expresstalk.dev.backend.service.SessionService;
import expresstalk.dev.backend.service.UserService;
import expresstalk.dev.backend.utils.Converter;
import expresstalk.dev.backend.utils.ValidationErrorChecker;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class WebSocketGroupChatController {
    private final UserService userService;
    private final GroupChatService groupChatService;
    private final ChatService chatService;
    private final SessionService sessionService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/group_chat/send_message")
    private void sendGroupChatMessage(@Payload SendChatMessageDto sendChatMessageDto, Message<?> message) {
        try {
            MessageHeaders headers = message.getHeaders();
            HttpSession session = (HttpSession) SimpMessageHeaderAccessor.getSessionAttributes(headers).get("session");
            sessionService.ensureSessionExistense(session);
            ValidationErrorChecker.<SendChatMessageDto>checkDtoForErrors(sendChatMessageDto);

            UUID chatId = chatService.verifyAndGetChatUUID(sendChatMessageDto.chatId());
            UUID userId = sessionService.getUserIdFromSession(session);
            User sender = userService.findById(userId);
            GroupChat groupChat = groupChatService.getChat(chatId);
            groupChatService.ensureUserExistsInChat(sender, groupChat);
            List<User> receivers = groupChatService.getOtherUsersOfChat(sender, groupChat);
            GroupMessage groupMessage = groupChatService.saveMessage(sender, sendChatMessageDto);
            GroupChatMessageDto groupChatMessageDto = new GroupChatMessageDto(
                    groupMessage.getCreatedAt(),
                    groupMessage.getContent(),
                    groupMessage.getSender().getUser().getLogin(),
                    groupMessage.getSender().getGroupChatRole(),
                    sender.getId(),
                    groupMessage.getAttachedFile()
            );

            for(User receiver : receivers) {
                LastMessageDto lastMessageDto = new LastMessageDto(chatId,groupMessage.getContent());
                simpMessagingTemplate.convertAndSend("/chat/last_message/" + receiver.getId(), lastMessageDto);
            }

            simpMessagingTemplate.convertAndSend("/group_chat/messages/" + sendChatMessageDto.chatId(), groupChatMessageDto);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            simpMessagingTemplate.convertAndSend("/group_chat/messages/" + sendChatMessageDto.chatId() + "/errors", ex.getMessage());
        }
    }

    @MessageMapping("/group_chat/add_member")
    private void addMemberToGroupChat(@Payload AddUserToGroupChatDto addUserToGroupChatDto, Message<?> message) {
        try {
            MessageHeaders headers = message.getHeaders();
            HttpSession session = (HttpSession) SimpMessageHeaderAccessor.getSessionAttributes(headers).get("session");
            sessionService.ensureSessionExistense(session);
            ValidationErrorChecker.<AddUserToGroupChatDto>checkDtoForErrors(addUserToGroupChatDto);

            UUID chatId = chatService.verifyAndGetChatUUID(addUserToGroupChatDto.chatId());
            UUID userId = sessionService.getUserIdFromSession(session);
            User admin = userService.findById(userId);
            User member = userService.findById(Converter.convertStringToUUID(addUserToGroupChatDto.memberId()));
            GroupChat groupChat = groupChatService.getChat(chatId);
            groupChatService.addMemberToChat(admin, member, groupChat);
            String addMemberMessage = admin.getLogin() + " has added " + member.getLogin();
            SystemMessage systemMessage = chatService.saveSystemMessage(addMemberMessage, groupChat);
            UpdatedMembersDto updatedMembersDto = new UpdatedMembersDto(chatId, groupChat.getMembers());
            List<User> receivers = groupChatService.getOtherUsersOfChat(admin, groupChat);

            for(User receiver : receivers) {
                LastMessageDto lastMessageDto = new LastMessageDto(chatId,addMemberMessage);
                simpMessagingTemplate.convertAndSend("/chat/last_message/" + receiver.getId(), lastMessageDto);
            }
            simpMessagingTemplate.convertAndSend("/group_chat/system_messages/" + addUserToGroupChatDto.chatId(), systemMessage);
            simpMessagingTemplate.convertAndSend("/group_chat/updated_members/" + addUserToGroupChatDto.chatId(), updatedMembersDto);
        } catch (Exception ex) {
            simpMessagingTemplate.convertAndSend("/group_chat/add_member/" + addUserToGroupChatDto.chatId() + "/errors", ex.getMessage());
        }
    }

    @MessageMapping("/group_chat/remove_member")
    private void removeMemberFromGroupChat(@Payload RemoveUserFromGroupChatDto removeUserFromGroupChatDto, Message<?> message) {
        try {
            MessageHeaders headers = message.getHeaders();
            HttpSession session = (HttpSession) SimpMessageHeaderAccessor.getSessionAttributes(headers).get("session");
            sessionService.ensureSessionExistense(session);
            ValidationErrorChecker.<RemoveUserFromGroupChatDto>checkDtoForErrors(removeUserFromGroupChatDto);

            UUID chatId = chatService.verifyAndGetChatUUID(removeUserFromGroupChatDto.chatId());
            UUID userId = sessionService.getUserIdFromSession(session);
            User admin = userService.findById(userId);
            User member = userService.findById(Converter.convertStringToUUID(removeUserFromGroupChatDto.memberId()));
            GroupChat groupChat = groupChatService.getChat(chatId);
            groupChatService.removeMemberFromChat(admin, member, groupChat);
            String removeMemberMessage = admin.getLogin() + " has removed " + member.getLogin();
            SystemMessage systemMessage = chatService.saveSystemMessage(removeMemberMessage, groupChat);
            UpdatedMembersDto updatedMembersDto = new UpdatedMembersDto(chatId, groupChat.getMembers());
            List<User> receivers = groupChatService.getOtherUsersOfChat(admin, groupChat);

            for(User receiver : receivers) {
                LastMessageDto lastMessageDto = new LastMessageDto(chatId,removeMemberMessage);
                simpMessagingTemplate.convertAndSend("/chat/last_message/" + receiver.getId(), lastMessageDto);
            }
            simpMessagingTemplate.convertAndSend("/group_chat/system_messages/" + removeUserFromGroupChatDto.chatId(), systemMessage);
            simpMessagingTemplate.convertAndSend("/group_chat/updated_members/" + removeUserFromGroupChatDto.chatId(), updatedMembersDto);
        } catch (Exception ex) {
            simpMessagingTemplate.convertAndSend("/group_chat/remove_member/" + removeUserFromGroupChatDto.chatId() + "/errors", ex.getMessage());
        }
    }

    @MessageMapping("/group_chat/set_role")
    private void addMemberToGroupChat(@Payload SetUserRoleInGroupChatDto setUserRoleInGroupChatDto, Message<?> message) {
        try {
            MessageHeaders headers = message.getHeaders();
            HttpSession session = (HttpSession) SimpMessageHeaderAccessor.getSessionAttributes(headers).get("session");
            sessionService.ensureSessionExistense(session);
            ValidationErrorChecker.<SetUserRoleInGroupChatDto>checkDtoForErrors(setUserRoleInGroupChatDto);

            UUID chatId = chatService.verifyAndGetChatUUID(setUserRoleInGroupChatDto.chatId());
            UUID userId = sessionService.getUserIdFromSession(session);
            User admin = userService.findById(userId);
            User changingUser = userService.findById(Converter.convertStringToUUID(setUserRoleInGroupChatDto.userToGiveRoleId()));
            GroupChat groupChat = groupChatService.getChat(chatId);
            groupChatService.setRole(admin, changingUser, setUserRoleInGroupChatDto.groupChatRole(), groupChat);
            String changedRoleMessage = changingUser.getLogin() + " is now " + setUserRoleInGroupChatDto.groupChatRole();
            SystemMessage systemMessage = chatService.saveSystemMessage(changedRoleMessage, groupChat);
            UpdatedMembersDto updatedMembersDto = new UpdatedMembersDto(chatId, groupChat.getMembers());
            List<User> receivers = groupChatService.getOtherUsersOfChat(admin, groupChat);

            for(User receiver : receivers) {
                LastMessageDto lastMessageDto = new LastMessageDto(chatId,changedRoleMessage);
                simpMessagingTemplate.convertAndSend("/chat/last_message/" + receiver.getId(), lastMessageDto);
            }
            simpMessagingTemplate.convertAndSend("/group_chat/system_messages/" + setUserRoleInGroupChatDto.chatId(), systemMessage);
            simpMessagingTemplate.convertAndSend("/group_chat/updated_members/" + setUserRoleInGroupChatDto.chatId(), updatedMembersDto);
        } catch (Exception ex) {
            simpMessagingTemplate.convertAndSend("/group_chat/set_role/" + setUserRoleInGroupChatDto.chatId() + "/errors", ex.getMessage());
        }
    }
}
