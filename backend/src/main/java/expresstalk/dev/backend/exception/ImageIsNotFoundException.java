package expresstalk.dev.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class ImageIsNotFoundException extends ResponseStatusException {
    public ImageIsNotFoundException(UUID id) {
        super(HttpStatus.NOT_FOUND, "Image with id " + id + " wasn't found");
    }

    public ImageIsNotFoundException(HttpStatus status) {
        super(status);
    }

    public ImageIsNotFoundException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public ImageIsNotFoundException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
