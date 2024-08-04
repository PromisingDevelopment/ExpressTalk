package expresstalk.dev.backend.controller;

import expresstalk.dev.backend.dto.request.SendChatMessageDto;
import expresstalk.dev.backend.dto.response.LastMessageDto;
import expresstalk.dev.backend.dto.response.PrivateChatMessageDto;
import expresstalk.dev.backend.entity.PrivateChat;
import expresstalk.dev.backend.entity.PrivateMessage;
import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.service.ChatService;
import expresstalk.dev.backend.service.PrivateChatService;
import expresstalk.dev.backend.service.SessionService;
import expresstalk.dev.backend.service.UserService;
import expresstalk.dev.backend.utils.ValidationErrorChecker;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class WebSocketPrivateChatController {
    private final UserService userService;
    private final PrivateChatService privateChatService;
    private final ChatService chatService;
    private final SessionService sessionService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/private_chat/send_message")
    private void sendPrivateChatMessage(@Payload SendChatMessageDto sendChatMessageDto, Message<?> message) {
        try {
            MessageHeaders headers = message.getHeaders();
            HttpSession session = (HttpSession) SimpMessageHeaderAccessor.getSessionAttributes(headers).get("session");
            sessionService.ensureSessionExistense(session);
            ValidationErrorChecker.<SendChatMessageDto>checkDtoForErrors(sendChatMessageDto);

            UUID chatId = chatService.verifyAndGetChatUUID(sendChatMessageDto.chatId());
            UUID userId = sessionService.getUserIdFromSession(session);
            User sender = userService.findById(userId);
            PrivateChat privateChat = privateChatService.getChat(chatId);

            privateChatService.ensureUserPermissionToSendMessageInChat(sender, privateChat);

            User receiver = privateChatService.getSecondUserOfChat(sender, privateChat);
            PrivateMessage privateMessage = privateChatService.saveMessage(sendChatMessageDto, sender, receiver);
            PrivateChatMessageDto privateChatMessageDto = new PrivateChatMessageDto(
                    privateMessage.getCreatedAt(),
                    privateMessage.getContent(),
                    sender.getLogin(),
                    sender.getId(),
                    privateMessage.getAttachedFile()
            );
            LastMessageDto lastMessageDto = new LastMessageDto(chatId,privateMessage.getContent());

            simpMessagingTemplate.convertAndSend("/chat/last_message/" + receiver.getId(), lastMessageDto);
            simpMessagingTemplate.convertAndSend("/chat/last_message/" + sender.getId(), lastMessageDto);
            simpMessagingTemplate.convertAndSend("/private_chat/messages/" + sendChatMessageDto.chatId(), privateChatMessageDto);
        } catch (Exception ex) {
            simpMessagingTemplate.convertAndSend("/private_chat/messages/" + sendChatMessageDto.chatId() + "/errors", ex.getMessage());
        }
    }
}
