package expresstalk.dev.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class ChatNotFoundException extends ResponseStatusException {
    public ChatNotFoundException(UUID id) {
        super(HttpStatus.NOT_FOUND, "Chat with id " + id + " wasn't found");
    }

    public ChatNotFoundException(HttpStatus status) {
        super(status);
    }

    public ChatNotFoundException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public ChatNotFoundException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
