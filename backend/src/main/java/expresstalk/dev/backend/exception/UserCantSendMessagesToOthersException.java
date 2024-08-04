package expresstalk.dev.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class UserCantSendMessagesToOthersException extends ResponseStatusException {
    public UserCantSendMessagesToOthersException(UUID id) {
        super(HttpStatus.FORBIDDEN, "User with id " + id + " can't send messages to other people's chat");
    }

    public UserCantSendMessagesToOthersException(HttpStatus status) {
        super(status);
    }

    public UserCantSendMessagesToOthersException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public UserCantSendMessagesToOthersException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
