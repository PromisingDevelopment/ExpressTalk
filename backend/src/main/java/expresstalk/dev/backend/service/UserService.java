package expresstalk.dev.backend.service;

import expresstalk.dev.backend.dto.request.EditUserDto;
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


    @Transactional
    public User findById(UUID userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null) throw new UserNotFoundException(userId);

        return user;
    }

    @Transactional
    public User findByLogin(String login) {
        User user = userRepository.findUserByLogin(login);
        if(user == null) {
            throw new UserNotFoundException(HttpStatus.NOT_FOUND, "User with login: " + login + " doesn't exist");
        }

        return user;
    }

    @Transactional
    public User findUserByLoginOrEmail(String login, String email) {
        User user = userRepository.findUserByLoginOrEmail(login, email);

        if(user == null) {
            throw new UserNotFoundException(HttpStatus.NOT_FOUND, "User doesn't exist.");
        }

        return user;
    }

    @Transactional
    public void setAvatarImage(User user, MultipartFile avatarImage) {
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
    }

    @Transactional
    public byte[] getAvatarImage(User user) {
        AvatarFile image = user.getAvatarFile();
        if(image == null) {
            throw new ImageNotFoundException(HttpStatus.NOT_FOUND, "User with id " + user.getId() + " does not have avatar image");
        }

        return image.getData();
    }

    public User editUser(User currentUser, EditUserDto editUserDto) {
        if(currentUser == null) throw new InternalServerErrorException();

        currentUser.setLogin(editUserDto.login());
        currentUser.setName(editUserDto.name());

        userRepository.save(currentUser);

        return currentUser;
    }
}
