package expresstalk.dev.backend.service;

import expresstalk.dev.backend.dto.response.ImageId;
import expresstalk.dev.backend.entity.AvatarFile;
import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.enums.UserStatus;
import expresstalk.dev.backend.exception.ImageNotFoundException;
import expresstalk.dev.backend.exception.InternalServerErrorException;
import expresstalk.dev.backend.exception.InvalidFileTypeException;
import expresstalk.dev.backend.exception.UserNotFoundException;
import expresstalk.dev.backend.repository.AvatarFileRepository;
import expresstalk.dev.backend.repository.UserRepository;
import expresstalk.dev.backend.utils.ImageUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final AvatarFileRepository avatarFileRepository;

    public void handleStatusTo(UUID userId, UserStatus userStatus) {
        User user = findById(userId);
        user.setStatus(userStatus);

        userRepository.save(user);
    }

    public User findById(UUID userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null) throw new UserNotFoundException(userId);

        return user;
    }

    public User findByLogin(String login) {
        User user = userRepository.findUserByLogin(login);
        if(user == null) {
            throw new UserNotFoundException(HttpStatus.NOT_FOUND, "User with login: " + login + " doesn't exist");
        }

        return user;
    }

    public ImageId setAvatarImage(User user, MultipartFile avatarImage) {
        byte[] data;
        try {
            ImageUtils.validateImage(avatarImage);
            data = avatarImage.getBytes();
        } catch (InvalidObjectException exception) {
            throw new InvalidFileTypeException("image");
        } catch (IOException exception) {
            throw new InternalServerErrorException(exception.getMessage());
        }

        AvatarFile avatarFileEntity = new AvatarFile(
                avatarImage.getOriginalFilename(),
                avatarImage.getContentType(),
                data
        );

        if(user.getAvatarFile() != null) {
            avatarFileRepository.delete(user.getAvatarFile());
        }

        user.setAvatarFile(avatarFileEntity);
        avatarFileRepository.save(avatarFileEntity);
        userRepository.save(user);

        return new ImageId(avatarFileEntity.getId());
    }

    @Transactional
    public byte[] getAvatarImage(UUID avatarId) {
        AvatarFile image = avatarFileRepository.findById(avatarId).orElse(null);
        if(image == null) {
            throw new ImageNotFoundException(avatarId);
        }

        return image.getData();
    }
}
