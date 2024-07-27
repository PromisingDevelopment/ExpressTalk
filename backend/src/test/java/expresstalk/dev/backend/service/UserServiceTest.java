package expresstalk.dev.backend.service;

import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.enums.UserStatus;
import expresstalk.dev.backend.repository.UserRepository;
import expresstalk.dev.backend.test_utils.TestValues;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    void shouldHandleStatusTo() {
        User user = TestValues.getUser();

        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        userService.handleStatusTo(UUID.randomUUID(), UserStatus.ONLINE);
        assertEquals(user.getStatus(), UserStatus.ONLINE);
        userService.handleStatusTo(UUID.randomUUID(), UserStatus.OFFLINE);
        assertEquals(user.getStatus(), UserStatus.OFFLINE);
    }

    @Test
    void shouldFindById() {
        User user = TestValues.getUser();
        user.setId(UUID.randomUUID());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        assertDoesNotThrow(() -> userService.findById(user.getId()));
    }

    @Test
    void shouldNotFindById() {
        User user = TestValues.getUser();
        user.setId(UUID.randomUUID());

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> userService.findById(user.getId()));
    }

    @Test
    void shouldFindByLogin() {
        User user = TestValues.getUser();

        when(userRepository.findUserByLogin(user.getLogin())).thenReturn(user);
        assertDoesNotThrow(() -> userService.findByLogin(user.getLogin()));
    }

    @Test
    void shouldNotFindByLogin() {
        User user = TestValues.getUser();

        when(userRepository.findUserByLogin(user.getLogin())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> userService.findByLogin(user.getLogin()));
    }
}