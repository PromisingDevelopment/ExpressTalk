package expresstalk.dev.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserIsNotAdminException extends ResponseStatusException {
    public UserIsNotAdminException() {
        super(HttpStatus.UNAUTHORIZED, "User can not perform action that are allowed only for admin");
    }

    public UserIsNotAdminException(HttpStatus status) {
        super(status);
    }

    public UserIsNotAdminException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public UserIsNotAdminException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
