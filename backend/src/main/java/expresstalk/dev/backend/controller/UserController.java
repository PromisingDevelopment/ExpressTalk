package expresstalk.dev.backend.controller;

import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{loginOrId}")
    public User getUserByLoginOrId(@PathVariable String loginOrId) {
        return userService.findByLoginOrId(loginOrId);
    }
}
