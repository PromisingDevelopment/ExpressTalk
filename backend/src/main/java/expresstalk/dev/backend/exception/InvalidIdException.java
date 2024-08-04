package expresstalk.dev.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidIdException extends ResponseStatusException {
    public InvalidIdException(String subject) {
        super(HttpStatus.BAD_REQUEST, "Provided " + subject + " id in is not UUID");
    }

    public InvalidIdException(HttpStatus status) {
        super(status);
    }

    public InvalidIdException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public InvalidIdException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
