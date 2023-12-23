package expresstalk.dev.backend.service;

import expresstalk.dev.backend.entity.User;
import expresstalk.dev.backend.enums.UserStatus;
import expresstalk.dev.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void handleStatusTo(UUID userId, UserStatus userStatus) {
        User user = userRepository.findById(userId).orElse(null);

        if(user == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't handle user's status due to invalid id provided.");
        }

        user.setStatus(userStatus);

        userRepository.save(user);
    }
}
