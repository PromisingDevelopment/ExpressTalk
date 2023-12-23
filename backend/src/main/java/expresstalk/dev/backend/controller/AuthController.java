package expresstalk.dev.backend.controller;

import expresstalk.dev.backend.service.AuthService;
import expresstalk.dev.backend.dto.EmailVerificationDto;
import expresstalk.dev.backend.dto.SignInUserDto;
import expresstalk.dev.backend.dto.SignUpUserDto;
import expresstalk.dev.backend.service.EmailService;
import expresstalk.dev.backend.exception.EmailNotVerifiedException;
import expresstalk.dev.backend.entity.User;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
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

        return "redirect:http://localhost:8080/email-verification";
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/sign-in")
    public String signIn(@RequestBody @Valid SignInUserDto signInUserDto, HttpSession session) {
        User signedUser = new User();

        try {
            signedUser = authService.signIn(signInUserDto);
        } catch (Exception ex) {
            if(ex instanceof EmailNotVerifiedException) {
                return "redirect:http://localhost:8080/email-verification";
            }

            throw ex;
        }

        session.setAttribute("userId", signedUser.getId().toString());

        return "";
    }

    @PostMapping("/email-verification")
    public String makeEmailVerification(@RequestBody @Valid EmailVerificationDto emailVerificationDto, HttpSession session) {
        User verifiedUser = authService.makeEmailVerification(emailVerificationDto);

        session.setAttribute("userId", verifiedUser.getId().toString());

        return "";
    }
}
