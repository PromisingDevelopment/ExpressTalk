package expresstalk.dev.backend.service;

import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.enums.UserStatus;
import expresstalk.dev.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public void handleStatusTo(UUID userId, UserStatus userStatus) {
        User user = findById(userId);

        user.setStatus(userStatus);
        userRepository.save(user);
    }

    public User findById(UUID userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id: " + userId + " doesn't exist");
        }

        return user;
    }

    private User findByLogin(String login) {
        User user = userRepository.findUserByLogin(login);
        if(user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with login: " + login + " doesn't exist");
        }

        return user;
    }

    public User findByLoginOrId(String loginOrId) {
        UUID id = UUID.randomUUID();
        String login = loginOrId;
        try {
            id = UUID.fromString(loginOrId);
        } catch (Exception ex) {
            return findByLogin(login);
        }

        return findById(id);
    }
}
