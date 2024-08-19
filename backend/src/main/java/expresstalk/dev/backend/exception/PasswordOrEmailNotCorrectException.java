package expresstalk.dev.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PasswordOrEmailNotCorrectException extends ResponseStatusException {
    public PasswordOrEmailNotCorrectException() {
        super(HttpStatus.BAD_REQUEST, "Password or email is not correct");
    }

    public PasswordOrEmailNotCorrectException(HttpStatus status) {
        super(status);
    }

    public PasswordOrEmailNotCorrectException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public PasswordOrEmailNotCorrectException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
