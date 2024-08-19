package expresstalk.dev.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IncorrectEmailCodeException extends ResponseStatusException {
    public IncorrectEmailCodeException() {
        super(HttpStatus.BAD_REQUEST, "Incorrect email code provided");
    }

    public IncorrectEmailCodeException(HttpStatus status) {
        super(status);
    }

    public IncorrectEmailCodeException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public IncorrectEmailCodeException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
