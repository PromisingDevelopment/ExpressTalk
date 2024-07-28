package expresstalk.dev.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class UserAbsentInChatException extends ResponseStatusException {
    public UserAbsentInChatException(UUID id) {
        super(HttpStatus.NOT_FOUND, "User with id " + id + " absent in the chat");
    }

    public UserAbsentInChatException(HttpStatus status) {
        super(status);
    }

    public UserAbsentInChatException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public UserAbsentInChatException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
