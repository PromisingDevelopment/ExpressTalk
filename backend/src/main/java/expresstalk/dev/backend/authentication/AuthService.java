package expresstalk.dev.backend.authentication;

import expresstalk.dev.backend.authentication.dto.SignInUserDto;
import expresstalk.dev.backend.authentication.dto.SignUpUserDTO;
import expresstalk.dev.backend.user.User;
import expresstalk.dev.backend.user.UserRepository;
import expresstalk.dev.backend.util.PhoneConverter;
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
        User existedUser = userRepository.findUserByLoginOrPhone(signUpUserDTO.login(), signUpUserDTO.phone());

        if(existedUser != null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is already exists");
        }

        String passwordHash = passwordEncoder.encode(signUpUserDTO.password());
        String formattedPhone = PhoneConverter.convertToDBFormat(signUpUserDTO.phone());

        User newUser = new User(
                signUpUserDTO.name(),
                signUpUserDTO.login(),
                formattedPhone,
                passwordHash
        );

        userRepository.save(newUser);
    };

    public void signIn(SignInUserDto signInUserDto) {
        User existedUser;
        String formattedPhone = "";

        // signInUserDto.login() can be login or phone
        // if signInUserDto.login() is login we pass assigning to formattedPhone
        // because convertToDBFormat throws error (it can't convert non-phones)
        try {
            formattedPhone = PhoneConverter.convertToDBFormat(signInUserDto.login());
        }
        catch (RuntimeException exception) {}

        existedUser = userRepository.findUserByLoginOrPhone(signInUserDto.login(), formattedPhone);

        if(existedUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist.");
        }

        Boolean isPasswordValid = passwordEncoder.matches(signInUserDto.password(), existedUser.getPasswordHash());

        if(!isPasswordValid) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Password or email is not correct.");
        }
    };

    public void makePhoneVerification() {};
}
