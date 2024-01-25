package expresstalk.dev.backend.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class SessionService {
    public boolean isSessionWithUserExists(HttpSession session) {
        return !(session.getAttribute("userId") == null);
    }

    public void ensureSessionExistense(HttpSession session) {
        if(!isSessionWithUserExists(session)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authenticated");
        }
    }

    public UUID getUserIdFromSession(HttpSession session) {
        ensureSessionExistense(session);

        return UUID.fromString(session.getAttribute("userId").toString());
    }
}
