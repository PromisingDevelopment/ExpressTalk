package expresstalk.dev.backend.controller;

import expresstalk.dev.backend.enums.UserStatus;
import expresstalk.dev.backend.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController("/")
public class ChatController {
    private final UserService userService;

    public ChatController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public String getChatsPage(HttpSession session) {
        if(session.getAttribute("userId") == null) {
            return "redirect:http://localhost:8080/auth";
        }

        UUID userId = UUID.fromString(session.getAttribute("userId").toString());

        userService.handleStatusTo(userId, UserStatus.ONLINE);

        return "";
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/log-out")
    public String deleteSession(HttpSession session) {
        session.invalidate();

        return "redirect:http://localhost:8080/auth";
    }
}
