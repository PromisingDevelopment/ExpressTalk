package expresstalk.dev.backend.authentication;

import expresstalk.dev.backend.authentication.dto.SignInUserDto;
import expresstalk.dev.backend.authentication.dto.SignUpUserDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/sign-up")
    public void signUp(@RequestBody @Valid SignUpUserDTO signUpUserDTO) {
        authService.signUp(signUpUserDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/sign-in")
    public void signIn(@RequestBody @Valid SignInUserDto signInUserDto) {
        authService.signIn(signInUserDto);
    }

    @PostMapping("/email-verification")
    public void makeEmailVerification() {
        authService.makeEmailVerification();
    }
}
