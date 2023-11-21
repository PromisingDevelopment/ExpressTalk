package expresstalk.dev.backend.authentication;

import expresstalk.dev.backend.authentication.dto.SignInUserDto;
import expresstalk.dev.backend.authentication.dto.SignUpUserDTO;
import expresstalk.dev.backend.user.User;
import expresstalk.dev.backend.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signUp(SignUpUserDTO signUpUserDTO) {
        User existedUser = userRepository.findUserByLoginOrEmail(signUpUserDTO.login(), signUpUserDTO.email());

        if(existedUser != null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is already exists");
        }

        String passwordHash = passwordEncoder.encode(signUpUserDTO.password());

        User newUser = new User(
                signUpUserDTO.name(),
                signUpUserDTO.login(),
                signUpUserDTO.email(),
                passwordHash
        );

        userRepository.save(newUser);
    };

    public void signIn(SignInUserDto signInUserDto) {
        User existedUser;
        String formattedPhone = "";

        // signInUserDto.login() can be login or email
        existedUser = userRepository.findUserByLoginOrEmail(signInUserDto.login(), signInUserDto.login());

        if(existedUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist.");
        }

        Boolean isPasswordValid = passwordEncoder.matches(signInUserDto.password(), existedUser.getPasswordHash());

        if(!isPasswordValid) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Password or email is not correct.");
        }
    };

    public void makeEmailVerification() {};
}
