package expresstalk.dev.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotAuthenticatedException extends ResponseStatusException {
    public UserNotAuthenticatedException() {
        super(HttpStatus.UNAUTHORIZED, "User is not authenticated");
    }

    public UserNotAuthenticatedException(HttpStatus status) {
        super(status);
    }

    public UserNotAuthenticatedException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public UserNotAuthenticatedException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
