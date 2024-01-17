package expresstalk.dev.backend.controller;

import expresstalk.dev.backend.dto.*;
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
import jakarta.servlet.http.HttpSession;
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
            HttpSession session = (HttpSession)SimpMessageHeaderAccessor.getSessionAttributes(headers).get("session");
            sessionService.ensureSessionExistense(session);

            UUID chatId = UUID.randomUUID();
            try {
                chatId = Converter.convertStringToUUID(chatStrId);
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provided chat id in the path is not UUID");
            }

            ValidationErrorChecker.<SendPrivateChatMessageDto>checkDtoForErrors(sendPrivateChatMessageDto);

            UUID userId = sessionService.getUserIdFromSession(session);
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
    public GetUserChatsDto getChatsPage(HttpSession session) {
        UUID userId = sessionService.getUserIdFromSession(session);

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
    public PrivateChat getPrivateChatRoom(@PathVariable String chatStrId, HttpSession session) {
        sessionService.ensureSessionExistense(session);

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
            @ApiResponse(responseCode = "400", description = "Provided user id in the path is not UUID"),
            @ApiResponse(responseCode = "404", description = "User with provided id doesn't exist"),
            @ApiResponse(responseCode = "403", description = "User is not authenticated"),
            @ApiResponse(responseCode = "409", description = "Private chat with provided two members had already been created"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/private/{secondMemberStrId}")
    @ResponseBody
    public PrivateChat createPrivateChatRoom(@PathVariable String secondMemberStrId, HttpSession session) {
        UUID userId = sessionService.getUserIdFromSession(session);

        UUID secondMemberId = UUID.randomUUID();
        try {
            secondMemberId = Converter.convertStringToUUID(secondMemberStrId);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provided user id in the path is not UUID");
        }

        PrivateChat chat = chatService.createPrivateChat(userId, secondMemberId);

        return chat;
    }
}
