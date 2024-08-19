package expresstalk.dev.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotAdminException extends ResponseStatusException {
    public UserNotAdminException() {
        super(HttpStatus.UNAUTHORIZED, "User can not perform action that are allowed only for admin");
    }

    public UserNotAdminException(HttpStatus status) {
        super(status);
    }

    public UserNotAdminException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public UserNotAdminException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
