package expresstalk.dev.backend.service;

import expresstalk.dev.backend.dto.request.EmailVerificationDto;
import expresstalk.dev.backend.dto.request.SignInUserDto;
import expresstalk.dev.backend.dto.request.SignUpUserDto;
import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.exception.EmailNotVerifiedException;
import expresstalk.dev.backend.repository.UserRepository;
import expresstalk.dev.backend.test_utils.TestValues;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Spy
    private BCryptPasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthService authService;

    @Test
    void shouldSignUpNewUser() {
        User data = TestValues.getUser();
        SignUpUserDto signUpUserDto = new SignUpUserDto(
                data.getName(),
                data.getLogin(),
                data.getEmail(),
                TestValues.getPassword()
        );

        when(userRepository.findUserByLoginOrEmail(anyString(), anyString())).thenReturn(null);

        User result = authService.signUp(signUpUserDto);
        verify(userRepository).save(any(User.class));
        assertNotNull(result);
        assertEquals(result.getEmail(), data.getEmail());
    }

    @Test
    void shouldNotSignUpExistedUser() {
        User user = TestValues.getUser();
        SignUpUserDto signUpUserDto = new SignUpUserDto(
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                TestValues.getPassword()
        );

        when(userRepository.findUserByLoginOrEmail(anyString(), anyString())).thenReturn(user);

        assertThrows(ResponseStatusException.class, () -> authService.signUp(signUpUserDto));
        user.setEmailCode(TestValues.getEmailCode());
        assertThrows(EmailNotVerifiedException.class, () -> authService.signUp(signUpUserDto));
    }

    @Test
    void shouldSignIn() {
        User user = TestValues.getUser();
        SignInUserDto signInUserDto = new SignInUserDto(
                user.getName(),
                TestValues.getPassword()
        );

        when(userRepository.findUserByLoginOrEmail(anyString(), anyString())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        User result = assertDoesNotThrow(() -> authService.signIn(signInUserDto));
        assertNotNull(result);
        assertEquals(result.getLogin(), user.getLogin());
    }

    @Test
    void shouldNotSignIn() {
        User user = TestValues.getUser();
        SignInUserDto signInUserDto = new SignInUserDto(
                user.getName(),
                TestValues.getPassword()
        );

        when(userRepository.findUserByLoginOrEmail(anyString(), anyString())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> authService.signIn(signInUserDto));
        when(userRepository.findUserByLoginOrEmail(anyString(), anyString())).thenReturn(user);
        user.setEmailCode(TestValues.getEmailCode());
        assertThrows(ResponseStatusException.class, () -> authService.signIn(signInUserDto));
        user.setEmailCode(null);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> authService.signIn(signInUserDto));
    }

    @Test
    void shouldMakeEmailVerification() {
        User user = TestValues.getUser();
        user.setEmailCode(TestValues.getEmailCode());
        EmailVerificationDto emailVerificationDto = new EmailVerificationDto(
                user.getEmail(),
                TestValues.getEmailCode()
        );

        when(userRepository.findUserByLoginOrEmail(anyString(), anyString())).thenReturn(user);

        User result = assertDoesNotThrow(() -> authService.makeEmailVerification(emailVerificationDto));
        verify(userRepository).save(user);
        assertNull(result.getEmailCode());
    }

    @Test
    void shouldNotMakeEmailVerification() {
        User user = TestValues.getUser();
        EmailVerificationDto emailVerificationDto = new EmailVerificationDto(
                user.getEmail(),
                TestValues.getEmailCode()
        );

        when(userRepository.findUserByLoginOrEmail(anyString(), anyString())).thenReturn(user);

        assertThrows(ResponseStatusException.class, () -> authService.makeEmailVerification(emailVerificationDto));
    }
}