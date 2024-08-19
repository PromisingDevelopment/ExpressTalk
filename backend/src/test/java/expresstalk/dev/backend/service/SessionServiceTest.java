package expresstalk.dev.backend.service;

import expresstalk.dev.backend.exception.UserNotAuthenticatedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class SessionServiceTest {
    @Mock
    private HttpSession httpSession;
    @Mock
    private HttpServletRequest httpServletRequest;
    @InjectMocks
    private SessionService sessionService;

    @Test
    void shouldVerifySessionWithUserExists() {
        when(httpSession.getAttribute(anyString())).thenReturn(UUID.randomUUID());
        assertTrue(sessionService.isSessionWithUserExists(httpSession));
    }

    @Test
    void shouldVerifySessionWithUserDoesNotExists() {
        when(httpSession.getAttribute(anyString())).thenReturn(null);
        assertFalse(sessionService.isSessionWithUserExists(httpSession));
    }

    @Test
    void shouldEnsureSessionExistense() {
        when(httpSession.getAttribute(anyString())).thenReturn(UUID.randomUUID());
        assertDoesNotThrow(() -> sessionService.ensureSessionExistense(httpSession));
        when(httpServletRequest.getSession(false)).thenReturn(httpSession);
        assertDoesNotThrow(() -> sessionService.ensureSessionExistense(httpServletRequest));
    }

    @Test
    void shouldThrowWhenSessionWithUserDoesNotExist() {
        assertThrows(UserNotAuthenticatedException.class, () -> sessionService.ensureSessionExistense(httpServletRequest));
        assertThrows(UserNotAuthenticatedException.class, () -> sessionService.ensureSessionExistense(httpSession));
        when(httpServletRequest.getSession(false)).thenReturn(httpSession);
        assertThrows(UserNotAuthenticatedException.class, () -> sessionService.ensureSessionExistense(httpServletRequest));
    }
}