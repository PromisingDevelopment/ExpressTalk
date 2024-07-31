package expresstalk.dev.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class InvalidFileTypeException extends ResponseStatusException {
    public InvalidFileTypeException(String neededFileType) {
        super(HttpStatus.FORBIDDEN, "Invalid file type provided. Only " + neededFileType.toLowerCase() + " acceptable");
    }

    public InvalidFileTypeException(HttpStatus status) {
        super(status);
    }

    public InvalidFileTypeException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public InvalidFileTypeException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
