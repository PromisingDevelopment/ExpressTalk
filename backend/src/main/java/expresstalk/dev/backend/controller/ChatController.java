package expresstalk.dev.backend.controller;

import expresstalk.dev.backend.dto.GetUserChatsDto;
import expresstalk.dev.backend.entity.PrivateChat;
import expresstalk.dev.backend.enums.UserStatus;
import expresstalk.dev.backend.service.ChatService;
import expresstalk.dev.backend.service.UserService;
import expresstalk.dev.backend.utils.RegexChecker;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/chats")
public class ChatController {
    private final UserService userService;
    private final ChatService chatService;

    public ChatController(UserService userService, ChatService chatService) {
        this.userService = userService;
        this.chatService = chatService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @ResponseBody
    public GetUserChatsDto getChatsPage(HttpSession session) {
        if(session.getAttribute("userId") == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authenticated");
        }

        UUID userId = UUID.fromString(session.getAttribute("userId").toString());

        userService.handleStatusTo(userId, UserStatus.ONLINE);

        return userService.getUserIdAndChats(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/log-out")
    public void deleteSession(HttpSession session) {
        session.invalidate();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/private/{chatId}")
    public PrivateChat getChatRoom(@PathVariable String chatId) {
        // regex for UUID_UUID format
        String regex = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}_[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

        if(!RegexChecker.isMatches(regex, chatId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect chat id provided.");
        }

        PrivateChat chat = chatService.createPrivateChatOrGetIfExists(chatId);

        return chat;
    }
}
