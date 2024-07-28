package expresstalk.dev.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class ChatIsNotFoundException extends ResponseStatusException {
    public ChatIsNotFoundException(UUID id) {
        super(HttpStatus.NOT_FOUND, "Chat with id " + id + " wasn't found");
    }

    public ChatIsNotFoundException(HttpStatus status) {
        super(status);
    }

    public ChatIsNotFoundException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public ChatIsNotFoundException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
