package expresstalk.dev.backend.controller;

import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.service.SessionService;
import expresstalk.dev.backend.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final SessionService sessionService;

    public UserController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

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
    public User getSelf(HttpSession session) {
        UUID userId = sessionService.getUserIdFromSession(session);

        return userService.findById(userId);
    }
}
