package expresstalk.dev.backend.controller;

import expresstalk.dev.backend.dto.request.CreatePrivateChatRoomDto;
import expresstalk.dev.backend.entity.PrivateChat;
import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.service.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Provided chat id in the path is not UUID"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated"),
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
            @ApiResponse(responseCode = "401", description = "User is not authenticated"),
            @ApiResponse(responseCode = "404", description = "User with provided id doesn't exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
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