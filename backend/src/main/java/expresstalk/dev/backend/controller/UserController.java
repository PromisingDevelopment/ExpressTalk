package expresstalk.dev.backend.controller;

import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
}
