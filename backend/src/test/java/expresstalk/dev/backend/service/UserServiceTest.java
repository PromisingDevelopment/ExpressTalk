package expresstalk.dev.backend.service;

import expresstalk.dev.backend.entity.AvatarImage;
import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.enums.UserStatus;
import expresstalk.dev.backend.exception.ImageIsNotFoundException;
import expresstalk.dev.backend.exception.UserIsNotFoundException;
import expresstalk.dev.backend.repository.AvatarImageRepository;
import expresstalk.dev.backend.repository.UserRepository;
import expresstalk.dev.backend.test_utils.TestValues;
import expresstalk.dev.backend.utils.ImageUtils;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AvatarImageRepository avatarImageRepository;
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
        assertThrows(UserIsNotFoundException.class, () -> userService.findById(user.getId()));
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
        assertThrows(UserIsNotFoundException.class, () -> userService.findByLogin(user.getLogin()));
    }

    @Test
    void shouldSetAvatarImage() {
        User user = TestValues.getUser();
        String imageName = TestValues.getWord();
        String extension = ".jpg";
        MultipartFile image1 = new MockMultipartFile(
                imageName,
                imageName + extension,
                "image/jpeg",
                TestValues.getSentence().getBytes()
        );

        assertDoesNotThrow(() -> userService.setAvatarImage(user, image1));

        verify(avatarImageRepository).save(any(AvatarImage.class));
        verify(userRepository).save(user);
        assertEquals(user.getAvatarImage().getName(), imageName + extension);

        imageName = TestValues.getWord();
        extension = ".png";
        MultipartFile image2 = new MockMultipartFile(
                imageName,
                imageName + extension,
                "image/png",
                TestValues.getSentence().getBytes()
        );

        assertDoesNotThrow(() -> userService.setAvatarImage(user, image2));

        assertEquals(user.getAvatarImage().getName(), imageName + extension);
    }

    @Test
    void shouldGetAvatarImage() {
        UUID avatarId = UUID.randomUUID();
        String imageName = TestValues.getWord();
        String extension = ".jpg";
        AvatarImage avatarImage = new AvatarImage();
        try {
            avatarImage = new AvatarImage(
                    imageName + extension,
                    "image/jpeg",
                    ImageUtils.compressImage(TestValues.getSentence().getBytes())
            );
        } catch (Exception exception) {
            fail("Unable to compress image");
        }

        when(avatarImageRepository.findById(avatarId)).thenReturn(Optional.of(avatarImage));
        assertDoesNotThrow(() -> userService.getAvatarImage(avatarId));
        when(avatarImageRepository.findById(avatarId)).thenReturn(Optional.empty());
        assertThrows(ImageIsNotFoundException.class, () -> userService.getAvatarImage(avatarId));
        when(avatarImageRepository.findById(avatarId)).thenReturn(Optional.of(new AvatarImage()));
        assertThrows(ContextedRuntimeException.class, () -> userService.getAvatarImage(avatarId));
    }
}