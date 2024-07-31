package expresstalk.dev.backend.controller;

import expresstalk.dev.backend.dto.response.PrivateChatMessageDto;
import expresstalk.dev.backend.dto.request.CreatePrivateChatRoomDto;
import expresstalk.dev.backend.dto.response.LastMessageDto;
import expresstalk.dev.backend.dto.request.SendChatMessageDto;
import expresstalk.dev.backend.entity.PrivateChat;
import expresstalk.dev.backend.entity.PrivateMessage;
import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.service.*;
import expresstalk.dev.backend.utils.ValidationErrorChecker;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/private_chats")
public class PrivateChatController {
    private final UserService userService;
    private final PrivateChatService privateChatService;
    private final ChatService chatService;
    private final SessionService sessionService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/private_chat/send_message")
    private void sendPrivateChatMessage(
            @Payload SendChatMessageDto sendChatMessageDto,
            Message<?> message
    ) {
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
                    sender.getId()
            );
            LastMessageDto lastMessageDto = new LastMessageDto(chatId,privateMessage.getContent());

            simpMessagingTemplate.convertAndSend("/chat/last_message/" + receiver.getId(), lastMessageDto);
            simpMessagingTemplate.convertAndSend("/chat/last_message/" + sender.getId(), lastMessageDto);
            simpMessagingTemplate.convertAndSend("/private_chat/messages/" + sendChatMessageDto.chatId(), privateChatMessageDto);
        } catch (Exception ex) {
            simpMessagingTemplate.convertAndSend("/private_chat/messages/" + sendChatMessageDto.chatId() + "/errors", ex.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Provided chat id in the path is not UUID"),
            @ApiResponse(responseCode = "403", description = "User is not authenticated"),
            @ApiResponse(responseCode = "404", description = "Chat with provided id doesn't exist."),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{chatStrId}")
    @ResponseBody
    public PrivateChat getPrivateChatRoom(@PathVariable String chatStrId, HttpServletRequest request) {
        sessionService.ensureSessionExistense(request);

        UUID chatId = chatService.verifyAndGetChatUUID(chatStrId);
        PrivateChat chat = privateChatService.getChat(chatId);

        return chat;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Second user's id must be a type of UUID"),
            @ApiResponse(responseCode = "400", description = "Can not create private chat with 1 person"),
            @ApiResponse(responseCode = "404", description = "User with provided id doesn't exist"),
            @ApiResponse(responseCode = "403", description = "User is not authenticated"),
            @ApiResponse(responseCode = "409", description = "Private chat with provided two members had already been created"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    @ResponseBody
    public PrivateChat createPrivateChatRoom(@RequestBody @Valid CreatePrivateChatRoomDto createPrivateChatRoomDto, HttpServletRequest request) {
        UUID user1Id = sessionService.getUserIdFromSession(request);
        UUID user2Id = UUID.fromString(createPrivateChatRoomDto.secondMemberId());
        User user1 = userService.findById(user1Id);
        User user2 = userService.findById(user2Id);
        PrivateChat chat = privateChatService.createPrivateChat(user1, user2);

        return chat;
    }
}