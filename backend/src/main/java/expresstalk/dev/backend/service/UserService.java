package expresstalk.dev.backend.service;

import expresstalk.dev.backend.dto.response.ImageId;
import expresstalk.dev.backend.entity.AvatarImage;
import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.enums.UserStatus;
import expresstalk.dev.backend.exception.ImageIsNotFoundException;
import expresstalk.dev.backend.exception.InvalidFileTypeException;
import expresstalk.dev.backend.exception.UserIsNotFoundException;
import expresstalk.dev.backend.repository.AvatarImageRepository;
import expresstalk.dev.backend.repository.UserRepository;
import expresstalk.dev.backend.utils.FileUtils;
import expresstalk.dev.backend.utils.ImageUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final AvatarImageRepository avatarImageRepository;

    public void handleStatusTo(UUID userId, UserStatus userStatus) {
        User user = findById(userId);
        user.setStatus(userStatus);

        userRepository.save(user);
    }

    public User findById(UUID userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null) throw new UserIsNotFoundException(userId);

        return user;
    }

    public User findByLogin(String login) {
        User user = userRepository.findUserByLogin(login);
        if(user == null) {
            throw new UserIsNotFoundException(HttpStatus.NOT_FOUND, "User with login: " + login + " doesn't exist");
        }

        return user;
    }

    public ImageId setAvatarImage(User user, MultipartFile avatarImage) {
        try {
            ImageUtils.validateImage(avatarImage);
        } catch (Exception exception) {
            throw new InvalidFileTypeException("image");
        }

        byte[] compressedData;
        try {
            compressedData = FileUtils.compressFile(avatarImage.getBytes());
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to compress image");
        }

        AvatarImage avatarImageEntity = new AvatarImage(
                avatarImage.getOriginalFilename(),
                avatarImage.getContentType(),
                compressedData
        );

        user.setAvatarImage(avatarImageEntity);
        avatarImageRepository.save(avatarImageEntity);
        userRepository.save(user);

        return new ImageId(avatarImageEntity.getId());
    }

    @Transactional
    public byte[] getAvatarImage(UUID avatarId) {
        AvatarImage image = avatarImageRepository.findById(avatarId).orElse(null);
        if(image == null) {
            throw new ImageIsNotFoundException(avatarId);
        }

        byte[] imageBytes;
        try {
            return FileUtils.decompressFile(image.getImageData());
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error downloading an image");
        }
    }
}
