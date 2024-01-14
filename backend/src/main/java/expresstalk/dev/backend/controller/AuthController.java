package expresstalk.dev.backend.controller;

import expresstalk.dev.backend.service.AuthService;
import expresstalk.dev.backend.dto.EmailVerificationDto;
import expresstalk.dev.backend.dto.SignInUserDto;
import expresstalk.dev.backend.dto.SignUpUserDto;
import expresstalk.dev.backend.service.EmailService;
import expresstalk.dev.backend.exception.EmailNotVerifiedException;
import expresstalk.dev.backend.entity.User;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "User's email needs verification"),
            @ApiResponse(responseCode = "403", description = "User already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/sign-up")
    public void signUp(@RequestBody @Valid SignUpUserDto signUpUserDto) {
        User user = new User();

        try {
            user = authService.signUp(signUpUserDto);
        } catch (Exception ex) {
            if(ex instanceof EmailNotVerifiedException) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User with these data registered but not confirmed. Please, confirm your email.");
            }

            throw ex;
        }

        try {
            emailService.sendEmailWithCode(user.getEmail(), user.getEmailCode());
        } catch (MessagingException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Email sending error. Try again later.");
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "User's email needs verification"),
            @ApiResponse(responseCode = "403", description = "Password or email is not correct."),
            @ApiResponse(responseCode = "404", description = "User doesn't exist."),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/sign-in")
    public void signIn(@RequestBody @Valid SignInUserDto signInUserDto, HttpSession session) {
        User signedUser = new User();

        try {
            signedUser = authService.signIn(signInUserDto);
        } catch (Exception ex) {
            if(ex instanceof EmailNotVerifiedException) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User with these data registered but not confirmed. Please, confirm your email.");
            }

            throw ex;
        }

        session.setAttribute("userId", signedUser.getId().toString());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Incorrect code provided."),
            @ApiResponse(responseCode = "409", description = "User's email has already verified."),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/email-verification")
    public void makeEmailVerification(@RequestBody @Valid EmailVerificationDto emailVerificationDto, HttpSession session) {
        User verifiedUser = authService.makeEmailVerification(emailVerificationDto);

        session.setAttribute("userId", verifiedUser.getId().toString());
    }
}
