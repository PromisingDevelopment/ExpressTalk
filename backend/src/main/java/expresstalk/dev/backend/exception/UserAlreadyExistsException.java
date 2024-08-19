package expresstalk.dev.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class UserAlreadyExistsException extends ResponseStatusException {
    public UserAlreadyExistsException() {
        super(HttpStatus.FORBIDDEN, "User already exists");
    }

    public UserAlreadyExistsException(HttpStatus status) {
        super(status);
    }

    public UserAlreadyExistsException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public UserAlreadyExistsException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
