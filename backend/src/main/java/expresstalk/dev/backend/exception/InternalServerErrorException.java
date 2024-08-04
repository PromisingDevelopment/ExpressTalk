package expresstalk.dev.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class InternalServerErrorException extends ResponseStatusException {
    public InternalServerErrorException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error: " + message);
    }

    public InternalServerErrorException(HttpStatus status) {
        super(status);
    }

    public InternalServerErrorException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public InternalServerErrorException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
