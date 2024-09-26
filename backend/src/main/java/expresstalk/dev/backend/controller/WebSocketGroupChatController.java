package expresstalk.dev.backend.controller;

import expresstalk.dev.backend.dto.request.*;
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
    private void sendGroupMessage(@Payload SendChatMessageDto sendChatMessageDto, Message<?> message) {
        try {
            MessageHeaders headers = message.getHeaders();
            HttpSession session = (HttpSession) SimpMessageHeaderAccessor.getSessionAttributes(headers).get("session");
            ValidationErrorChecker.<SendChatMessageDto>checkDtoForErrors(sendChatMessageDto);

            UUID chatId = chatService.verifyAndGetChatUUID(sendChatMessageDto.chatId());
            UUID userId = sessionService.getUserIdFromSession(session);
            User sender = userService.findById(userId);
            GroupChat groupChat = groupChatService.getChat(chatId);
            groupChatService.ensureUserExistsInChat(sender, groupChat);
            GroupMessage groupMessage = groupChatService.saveMessage(sender, sendChatMessageDto);
            GroupChatMessageDto groupChatMessageDto = new GroupChatMessageDto(
                    groupMessage.getId(),
                    groupMessage.getCreatedAt(),
                    groupMessage.getContent(),
                    groupMessage.getAttachedFile(),
                    groupMessage.getSender().getUser().getLogin(),
                    sender.getId(),
                    groupMessage.getSender().getGroupChatRole()
            );
            List<User> receivers = groupChatService.getUsersOfGroupChat(groupChat);

            for(User receiver : receivers) {
                LastMessageDto lastMessageDto = new LastMessageDto(chatId,groupMessage.getContent());
                simpMessagingTemplate.convertAndSend("/chat/last_message/" + receiver.getId(), lastMessageDto);
            }

            simpMessagingTemplate.convertAndSend("/group_chat/messages/" + chatId, groupChatMessageDto);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            simpMessagingTemplate.convertAndSend("/group_chat/messages/" + sendChatMessageDto.chatId() + "/errors", ex.getMessage());
        }
    }

    @MessageMapping("/group_chat/add_member")
    private void addMember(@Payload AddUserToGroupChatDto addUserToGroupChatDto, Message<?> message) {
        try {
            MessageHeaders headers = message.getHeaders();
            HttpSession session = (HttpSession) SimpMessageHeaderAccessor.getSessionAttributes(headers).get("session");
            ValidationErrorChecker.<AddUserToGroupChatDto>checkDtoForErrors(addUserToGroupChatDto);

            UUID chatId = chatService.verifyAndGetChatUUID(addUserToGroupChatDto.chatId());
            UUID userId = sessionService.getUserIdFromSession(session);
            User admin = userService.findById(userId);
            User member = userService.findById(Converter.convertStringToUUID(addUserToGroupChatDto.memberId()));
            GroupChat groupChat = groupChatService.getChat(chatId);
            groupChatService.addMember(admin, member, groupChat);
            String addMemberMessage = admin.getLogin() + " has added " + member.getLogin();
            SystemMessage systemMessage = chatService.saveSystemMessage(addMemberMessage, groupChat);
            UpdatedMembersDto updatedMembersDto = new UpdatedMembersDto(chatId, groupChat.getMembers());
            List<User> receivers = groupChatService.getUsersOfGroupChat(groupChat);

            for(User receiver : receivers) {
                LastMessageDto lastMessageDto = new LastMessageDto(chatId,addMemberMessage);
                simpMessagingTemplate.convertAndSend("/chat/last_message/" + receiver.getId(), lastMessageDto);
            }
            simpMessagingTemplate.convertAndSend("/group_chat/system_messages/" + chatId, systemMessage);
            simpMessagingTemplate.convertAndSend("/group_chat/updated_members/" + chatId, updatedMembersDto);
        } catch (Exception ex) {
            simpMessagingTemplate.convertAndSend("/group_chat/add_member/" + addUserToGroupChatDto.chatId() + "/errors", ex.getMessage());
        }
    }

    @MessageMapping("/group_chat/remove_member")
    private void removeMember(@Payload RemoveUserFromGroupChatDto removeUserFromGroupChatDto, Message<?> message) {
        try {
            System.out.println(removeUserFromGroupChatDto.memberId());
            MessageHeaders headers = message.getHeaders();
            HttpSession session = (HttpSession) SimpMessageHeaderAccessor.getSessionAttributes(headers).get("session");
            ValidationErrorChecker.<RemoveUserFromGroupChatDto>checkDtoForErrors(removeUserFromGroupChatDto);

            UUID chatId = chatService.verifyAndGetChatUUID(removeUserFromGroupChatDto.chatId());
            UUID userId = sessionService.getUserIdFromSession(session);
            User admin = userService.findById(userId);
            User member = userService.findById(Converter.convertStringToUUID(removeUserFromGroupChatDto.memberId()));
            GroupChat groupChat = groupChatService.getChat(chatId);
            groupChatService.removeMember(admin, member, groupChat);
            groupChat = groupChatService.getChat(chatId);
            String removeMemberMessage = admin.getLogin() + " has removed " + member.getLogin();
            SystemMessage systemMessage = chatService.saveSystemMessage(removeMemberMessage, groupChat);
            UpdatedMembersDto updatedMembersDto = new UpdatedMembersDto(chatId, groupChat.getMembers());
            List<User> receivers = groupChatService.getUsersOfGroupChat(groupChat);

            for(User receiver : receivers) {
                LastMessageDto lastMessageDto = new LastMessageDto(chatId,removeMemberMessage);
                simpMessagingTemplate.convertAndSend("/chat/last_message/" + receiver.getId(), lastMessageDto);
            }
            simpMessagingTemplate.convertAndSend("/group_chat/system_messages/" + chatId, systemMessage);
            simpMessagingTemplate.convertAndSend("/group_chat/updated_members/" + chatId, updatedMembersDto);
        } catch (Exception ex) {
            simpMessagingTemplate.convertAndSend("/group_chat/remove_member/" + removeUserFromGroupChatDto.chatId() + "/errors", ex.getMessage());
        }
    }

    @MessageMapping("/group_chat/set_role")
    private void setRole(@Payload SetUserRoleInGroupChatDto setUserRoleInGroupChatDto, Message<?> message) {
        try {
            MessageHeaders headers = message.getHeaders();
            HttpSession session = (HttpSession) SimpMessageHeaderAccessor.getSessionAttributes(headers).get("session");
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
            List<User> receivers = groupChatService.getUsersOfGroupChat(groupChat);

            for(User receiver : receivers) {
                LastMessageDto lastMessageDto = new LastMessageDto(chatId,changedRoleMessage);
                simpMessagingTemplate.convertAndSend("/chat/last_message/" + receiver.getId(), lastMessageDto);
            }
            simpMessagingTemplate.convertAndSend("/group_chat/system_messages/" + chatId, systemMessage);
            simpMessagingTemplate.convertAndSend("/group_chat/updated_members/" + chatId, updatedMembersDto);
        } catch (Exception ex) {
            simpMessagingTemplate.convertAndSend("/group_chat/set_role/" + setUserRoleInGroupChatDto.chatId() + "/errors", ex.getMessage());
        }
    }

    @MessageMapping("/group_chat/edit")
    private void editGroup(@Payload EditGroupChatNameDto editGroupChatNameDto, Message<?> message) {
        try {
            MessageHeaders headers = message.getHeaders();
            HttpSession session = (HttpSession) SimpMessageHeaderAccessor.getSessionAttributes(headers).get("session");
            ValidationErrorChecker.<EditGroupChatNameDto>checkDtoForErrors(editGroupChatNameDto);

            UUID chatId = chatService.verifyAndGetChatUUID(editGroupChatNameDto.chatId());
            UUID userId = sessionService.getUserIdFromSession(session);
            User admin = userService.findById(userId);
            GroupChat groupChat = groupChatService.getChat(chatId);
            GroupChat editedGroupChat = groupChatService.editChat(editGroupChatNameDto.groupName(), admin, groupChat);

            simpMessagingTemplate.convertAndSend("/group_chat/edited/" + chatId, editedGroupChat);
        } catch (Exception exception) {
            simpMessagingTemplate.convertAndSend("/group_chat/edited/" + editGroupChatNameDto.chatId() + "/errors", exception.getMessage());
        }
    }

    @MessageMapping("/group_chat/remove/{chatStrId}")
    private void removeGroup(@DestinationVariable String chatStrId, Message<?> message) {
        try {
            MessageHeaders headers = message.getHeaders();
            HttpSession session = (HttpSession) SimpMessageHeaderAccessor.getSessionAttributes(headers).get("session");

            UUID chatId = chatService.verifyAndGetChatUUID(chatStrId);
            UUID userId = sessionService.getUserIdFromSession(session);
            User admin = userService.findById(userId);
            GroupChat groupChat = groupChatService.getChat(chatId);
            groupChatService.removeChat(admin, groupChat);

            simpMessagingTemplate.convertAndSend("/group_chat/removed/" + chatId, true);
        } catch (Exception exception) {
            simpMessagingTemplate.convertAndSend("/group_chat/removed/" + chatStrId + "/errors", exception.getMessage());
        }
    }

    @MessageMapping("/group_chat/leave/{chatStrId}")
    private void leave(@DestinationVariable String chatStrId, Message<?> message) {
        try {
            MessageHeaders headers = message.getHeaders();
            HttpSession session = (HttpSession) SimpMessageHeaderAccessor.getSessionAttributes(headers).get("session");

            UUID chatId = chatService.verifyAndGetChatUUID(chatStrId);
            UUID userId = sessionService.getUserIdFromSession(session);
            User member = userService.findById(userId);
            GroupChat groupChat = groupChatService.getChat(chatId);
            groupChatService.leave(member, groupChat);
            String leftMessage = member.getLogin() + " left the chat";
            SystemMessage systemMessage = chatService.saveSystemMessage(leftMessage, groupChat);
            UpdatedMembersDto updatedMembersDto = new UpdatedMembersDto(chatId, groupChat.getMembers());
            List<User> receivers = groupChatService.getUsersOfGroupChat(groupChat);

            for(User receiver : receivers) {
                LastMessageDto lastMessageDto = new LastMessageDto(chatId,leftMessage);
                simpMessagingTemplate.convertAndSend("/chat/last_message/" + receiver.getId(), lastMessageDto);
            }
            simpMessagingTemplate.convertAndSend("/group_chat/system_messages/" + chatId, systemMessage);
            simpMessagingTemplate.convertAndSend("/group_chat/updated_members/" + chatId, updatedMembersDto);
        } catch (Exception exception) {
            simpMessagingTemplate.convertAndSend("/group_chat/left/" + chatStrId + "/errors", exception.getMessage());
        }
    }
}
