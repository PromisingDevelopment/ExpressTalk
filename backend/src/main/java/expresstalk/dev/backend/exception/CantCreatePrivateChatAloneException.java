package expresstalk.dev.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CantCreatePrivateChatAloneException extends ResponseStatusException {
    public CantCreatePrivateChatAloneException() {
        super(HttpStatus.BAD_REQUEST, "Can not create private chat with 1 person");
    }

    public CantCreatePrivateChatAloneException(HttpStatus status) {
        super(status);
    }

    public CantCreatePrivateChatAloneException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public CantCreatePrivateChatAloneException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
