package expresstalk.dev.backend.authentication;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    public void signUp() {
        authService.signUp();
    }

    @PostMapping("/sign-in")
    public void signIn() {
        authService.signIn();
    }

    @PostMapping("/phone-verification")
    public void makePhoneVerification() {
        authService.makePhoneVerification();
    }
}
