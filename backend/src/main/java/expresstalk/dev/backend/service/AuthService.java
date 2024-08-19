package expresstalk.dev.backend.service;

import expresstalk.dev.backend.dto.request.EmailVerificationDto;
import expresstalk.dev.backend.dto.request.SignInUserDto;
import expresstalk.dev.backend.dto.request.SignUpUserDto;
import expresstalk.dev.backend.enums.UserStatus;
import expresstalk.dev.backend.exception.*;
import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.repository.UserRepository;
import expresstalk.dev.backend.utils.Generator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public User signUp(SignUpUserDto signUpUserDto) {
        User existedUser = userRepository.findUserByLoginOrEmail(signUpUserDto.login(), signUpUserDto.email());

        if(existedUser != null) {
            if(existedUser.getEmailCode() != null) {
                throw new EmailNotVerifiedException(HttpStatus.ACCEPTED, "User's email needs verification");
            }

            throw new UserAlreadyExistsException();
        }

        String passwordHash = passwordEncoder.encode(signUpUserDto.password());
        String emailCode = Generator.generateCode();

        User newUser = new User(
                signUpUserDto.name(),
                signUpUserDto.login(),
                signUpUserDto.email(),
                passwordHash
        );
        newUser.setEmailCode(emailCode);
        newUser.setStatus(UserStatus.OFFLINE);

        userRepository.save(newUser);

        return newUser;
    };


    public User signIn(SignInUserDto signInUserDto) {
        // signInUserDto.login() can be login or email
        User user = userService.findUserByLoginOrEmail(signInUserDto.login(), signInUserDto.login());

        if(user.getEmailCode() != null) {
            throw new EmailNotVerifiedException(HttpStatus.ACCEPTED, "User's email needs verification");
        }

        Boolean isPasswordValid = passwordEncoder.matches(signInUserDto.password(), user.getPasswordHash());

        if(!isPasswordValid) {
                throw new PasswordOrEmailNotCorrectException();
        }

        return user;
    };

    public User makeEmailVerification(EmailVerificationDto emailVerificationDto) {
        User existedUser = userRepository.findUserByLoginOrEmail(emailVerificationDto.email(), emailVerificationDto.email());

        if(existedUser == null) throw new UserNotFoundException(emailVerificationDto.email());
        if(existedUser.getEmailCode() == null) return existedUser;
        if(!emailVerificationDto.code().equals(existedUser.getEmailCode())) throw new IncorrectEmailCodeException();

        existedUser.setEmailCode(null);
        userRepository.save(existedUser);

        return existedUser;
    };
}
