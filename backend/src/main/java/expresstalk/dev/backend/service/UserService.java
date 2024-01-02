package expresstalk.dev.backend.service;

import expresstalk.dev.backend.dto.GetUserChatsDto;
import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.enums.UserStatus;
import expresstalk.dev.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void handleStatusTo(UUID userId, UserStatus userStatus) {
        User user = findById(userId);

        user.setStatus(userStatus);

        userRepository.save(user);
    }

    private User findById(UUID userId) {
        User user = userRepository.findById(userId).orElse(null);

        if(user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with id: " + userId + " doesn't exist");
        }

        return user;
    }

    private User findByLogin(String login) {
        User user = userRepository.findUserByLogin(login);

        if(user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with login: " + login + " doesn't exist");
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

    public GetUserChatsDto getUserIdAndChats(UUID userId) {
        User user = findById(userId);

        GetUserChatsDto getUserChatsDto = new GetUserChatsDto(userId, user.getPrivateChats(), user.getGroupChats());

        return getUserChatsDto;
    }
}
