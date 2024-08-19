package expresstalk.dev.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class ImageNotFoundException extends ResponseStatusException {
    public ImageNotFoundException(UUID id) {
        super(HttpStatus.NOT_FOUND, "Image with id " + id + " wasn't found");
    }

    public ImageNotFoundException(HttpStatus status) {
        super(status);
    }

    public ImageNotFoundException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public ImageNotFoundException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
