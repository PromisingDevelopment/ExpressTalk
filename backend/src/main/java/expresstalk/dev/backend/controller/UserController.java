package expresstalk.dev.backend.controller;

import expresstalk.dev.backend.dto.request.GetUserChatsDto;
import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.enums.UserStatus;
import expresstalk.dev.backend.service.ChatService;
import expresstalk.dev.backend.service.SessionService;
import expresstalk.dev.backend.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final SessionService sessionService;
    private final ChatService chatService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description =  "User with provided login doesn't exist"),
            @ApiResponse(responseCode = "404", description =  "User with provided id doesn't exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{loginOrId}")
    public User getUserByLoginOrId(@PathVariable String loginOrId) {
        return userService.findByLoginOrId(loginOrId);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "User is not authenticated"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/self")
    public User getSelf(HttpServletRequest request) {
        UUID userId = sessionService.getUserIdFromSession(request);

        return userService.findById(userId);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "User is not authenticated"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/chats")
    @ResponseBody
    public GetUserChatsDto getChatsPage(HttpServletRequest request) {
        request.getSession(false);
        UUID userId = sessionService.getUserIdFromSession(request);
        User user = userService.findById(userId);

        try {
            userService.handleStatusTo(userId, UserStatus.ONLINE);
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }

        return chatService.getChats(user);
    }
}
