package expresstalk.dev.backend.service;

import expresstalk.dev.backend.exception.UserNotAuthenticatedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class SessionService {
    public boolean isSessionWithUserExists(HttpSession session) {
        if(session != null) {
            return !(session.getAttribute("userId") == null);
        }

        return false;
    }

    public void ensureSessionExistense(HttpServletRequest request) {
        if(!isSessionWithUserExists(request.getSession(false))) throw new UserNotAuthenticatedException();
    }

    public void ensureSessionExistense(HttpSession session) {
        if(!isSessionWithUserExists(session)) throw new UserNotAuthenticatedException();
    }

    public UUID getUserIdFromSession(HttpServletRequest request) {
        ensureSessionExistense(request);

        return UUID.fromString(request.getSession(false).getAttribute("userId").toString());
    }

    public UUID getUserIdFromSession(HttpSession session) {
        ensureSessionExistense(session);

        return UUID.fromString(session.getAttribute("userId").toString());
    }
}
