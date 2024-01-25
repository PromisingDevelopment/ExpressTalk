package expresstalk.dev.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailNotVerifiedException extends ResponseStatusException {
    public EmailNotVerifiedException(HttpStatus status) {
        super(status);
    }

    public EmailNotVerifiedException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public EmailNotVerifiedException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
