package expresstalk.dev.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class UserIsNotFoundException extends ResponseStatusException {
    public UserIsNotFoundException(UUID id) {
        super(HttpStatus.NOT_FOUND, "User with id " + id + " wasn't found");
    }

    public UserIsNotFoundException(HttpStatus status) {
        super(status);
    }

    public UserIsNotFoundException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public UserIsNotFoundException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
