package expresstalk.dev.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class UserNotFoundException extends ResponseStatusException {
    public UserNotFoundException(UUID id) {
        super(HttpStatus.NOT_FOUND, "User with id " + id + " wasn't found");
    }

    public UserNotFoundException(String loginOrEmail) {
        super(HttpStatus.NOT_FOUND, "User " + loginOrEmail + " wasn't found");
    }

    public UserNotFoundException(HttpStatus status) {
        super(status);
    }

    public UserNotFoundException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public UserNotFoundException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
