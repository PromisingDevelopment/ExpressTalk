package expresstalk.dev.backend.controller;

import expresstalk.dev.backend.dto.ClientChatMessageDto;
import expresstalk.dev.backend.dto.CreatePrivateChatRoomDto;
import expresstalk.dev.backend.dto.LastMessageDto;
import expresstalk.dev.backend.dto.SendChatMessageDto;
import expresstalk.dev.backend.entity.PrivateChat;
import expresstalk.dev.backend.entity.PrivateChatMessage;
import expresstalk.dev.backend.entity.User;
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
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/private_chats")
public class PrivateChatController {
    private final UserService userService;
    private final PrivateChatService privateChatService;
    private final ChatService chatService;
    private final SessionService sessionService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public PrivateChatController(UserService userService, PrivateChatService privateChatService,
                          SessionService sessionService, SimpMessagingTemplate simpMessagingTemplate,
                                 ChatService chatService) {
        this.userService = userService;
        this.privateChatService = privateChatService;
        this.sessionService = sessionService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatService = chatService;
    }

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

            UUID chatId = chatService.checkAndGetChatUUID(sendChatMessageDto.chatId());
            UUID userId = sessionService.getUserIdFromSession(session);
            User sender = userService.findById(userId);

            privateChatService.ensureUserPermissionToSendMessageInChat(sender, chatId);

            User receiver = privateChatService.getSecondUserOfChat(sender.getId(), chatId);

            PrivateChatMessage privateChatMessage = privateChatService.saveMessage(sendChatMessageDto, sender.getId(), receiver.getId());
            ClientChatMessageDto clientChatMessageDto = new ClientChatMessageDto(
                    sender.getLogin(),
                    privateChatMessage.getContent(),
                    privateChatMessage.getCreatedAt()
            );
            LastMessageDto lastMessageDto = new LastMessageDto(chatId,privateChatMessage.getContent());

            simpMessagingTemplate.convertAndSend("/chats/last_messages/" + receiver.getId(), lastMessageDto);
            simpMessagingTemplate.convertAndSend("/private_chat/messages/" + sendChatMessageDto.chatId(), clientChatMessageDto);
        } catch (Exception ex) {
            System.out.println("\n" + ex.getMessage() + "\n");
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

        UUID chatId = chatService.checkAndGetChatUUID(chatStrId);
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
        UUID userId = sessionService.getUserIdFromSession(request);
        UUID secondMemberId = UUID.fromString(createPrivateChatRoomDto.secondMemberId());
        PrivateChat chat = privateChatService.createPrivateChat(userId, secondMemberId);

        return chat;
    }
}