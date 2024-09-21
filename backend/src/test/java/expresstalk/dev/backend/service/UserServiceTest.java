package expresstalk.dev.backend.service;

import expresstalk.dev.backend.entity.AvatarFile;
import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.enums.UserStatus;
import expresstalk.dev.backend.exception.ImageNotFoundException;
import expresstalk.dev.backend.exception.InvalidFileTypeException;
import expresstalk.dev.backend.exception.UserNotFoundException;
import expresstalk.dev.backend.repository.AvatarFileRepository;
import expresstalk.dev.backend.repository.UserRepository;
import expresstalk.dev.backend.test_utils.TestValues;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

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
    private AvatarFileRepository avatarFileRepository;
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
        assertThrows(UserNotFoundException.class, () -> userService.findById(user.getId()));
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
        assertThrows(UserNotFoundException.class, () -> userService.findByLogin(user.getLogin()));
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

        verify(avatarFileRepository).save(any(AvatarFile.class));
        verify(userRepository).save(user);
        assertEquals(user.getAvatarFile().getName(), imageName + extension);

        imageName = TestValues.getWord();
        extension = ".png";
        MultipartFile image2 = new MockMultipartFile(
                imageName,
                imageName + extension,
                "image/png",
                TestValues.getSentence().getBytes()
        );

        assertDoesNotThrow(() -> userService.setAvatarImage(user, image2));

        assertEquals(user.getAvatarFile().getName(), imageName + extension);
    }

    @Test
    void shouldNotSetAvatarImage() {
        User user = TestValues.getUser();
        String imageName = TestValues.getWord();
        String extension = ".docx";
        MultipartFile file1 = new MockMultipartFile(
                imageName,
                imageName + extension,
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                TestValues.getSentence().getBytes()
        );

        assertThrows(InvalidFileTypeException.class, () -> userService.setAvatarImage(user, file1));

        imageName = TestValues.getWord();
        extension = ".xls";
        MultipartFile file2 = new MockMultipartFile(
                imageName,
                imageName + extension,
                "application/vnd.ms-excel",
                TestValues.getSentence().getBytes()
        );

        assertThrows(InvalidFileTypeException.class, () -> userService.setAvatarImage(user, file2));

        imageName = TestValues.getWord();
        extension = ".txt";
        MultipartFile file3 = new MockMultipartFile(
                imageName,
                imageName + extension,
                "text/plain",
                TestValues.getSentence().getBytes()
        );

        assertThrows(InvalidFileTypeException.class, () -> userService.setAvatarImage(user, file3));

    }

    @Test
    void shouldGetAvatarImage() {
        User user = TestValues.getUser();
        String imageName = TestValues.getWord();
        String extension = ".jpg";
        AvatarFile avatarFile = new AvatarFile();
        try {
            avatarFile = new AvatarFile(
                    imageName + extension,
                    "image/jpeg",
                    TestValues.getSentence().getBytes()
            );
        } catch (Exception exception) {
            fail("Unable to compress image");
        }

        user.setAvatarFile(avatarFile);
        assertDoesNotThrow(() -> userService.getAvatarImage(user));
        user.setAvatarFile(null);
        assertThrows(ImageNotFoundException.class, () -> userService.getAvatarImage(user));
    }
}