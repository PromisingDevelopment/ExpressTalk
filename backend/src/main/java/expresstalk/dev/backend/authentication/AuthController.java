package expresstalk.dev.backend.authentication;

import expresstalk.dev.backend.authentication.dto.EmailVerificationDto;
import expresstalk.dev.backend.authentication.dto.SignInUserDto;
import expresstalk.dev.backend.authentication.dto.SignUpUserDto;
import expresstalk.dev.backend.email.EmailService;
import expresstalk.dev.backend.exception.EmailNotVerifiedException;
import expresstalk.dev.backend.user.User;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final EmailService emailService;

    public AuthController(AuthService authService, EmailService emailService) {
        this.authService = authService;
        this.emailService = emailService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/sign-up")
    public String signUp(@RequestBody @Valid SignUpUserDto signUpUserDto) {
        User user = new User();

        try {
            user = authService.signUp(signUpUserDto);
        } catch (Exception ex) {
            if(ex instanceof EmailNotVerifiedException) {
                return "redirect:http://localhost:8080/email-verification";
            }

            throw ex;
        }

        try {
            emailService.sendEmailWithCode(user.getEmail(), user.getEmailCode());
        } catch (MessagingException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Email sending error. Try again later.");
        }

        return "";
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/sign-in")
    public String signIn(@RequestBody @Valid SignInUserDto signInUserDto) {
        try {
            authService.signIn(signInUserDto);
        } catch (Exception ex) {
            if(ex instanceof EmailNotVerifiedException) {
                return "redirect:http://localhost:8080/email-verification";
            }

            throw ex;
        }

        return "";
    }

    // ToDo: Make code checking not delivering
    @PostMapping("/email-verification")
    public void makeEmailVerification(@RequestBody @Valid EmailVerificationDto emailVerificationDto) {
        authService.makeEmailVerification(emailVerificationDto);
    }
}
