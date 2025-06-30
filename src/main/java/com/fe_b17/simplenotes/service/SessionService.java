package com.fe_b17.simplenotes.service;

import com.fe_b17.simplenotes.dto.SessionResponse;
import com.fe_b17.simplenotes.exception.SessionNotFoundException;
import com.fe_b17.simplenotes.mapper.SessionMapper;
import com.fe_b17.simplenotes.models.RefreshToken;
import com.fe_b17.simplenotes.models.Session;
import com.fe_b17.simplenotes.models.User;
import com.fe_b17.simplenotes.repo.RefreshTokenRepo;
import com.fe_b17.simplenotes.repo.SessionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepo sessionRepo;
    private final RefreshTokenRepo refreshTokenRepo;
    private final UserService userService;

    public Session createSession(User user, String ipAddress, String userAgent) {
        log.debug("Creating new session for user {} from IP {} with agent {}", user.getEmail(), ipAddress, userAgent);
        Session session = new Session();
        session.setUser(user);
        session.setIpAddress(ipAddress);
        session.setUserAgent(userAgent);
        session.setActive(true);
        return sessionRepo.save(session);
    }

    public Session deactivateSession(UUID sessionId) {
        Session session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(sessionId));
        session.setActive(false);
        return sessionRepo.save(session);
    }

    public boolean isActive(UUID sessionId){
        return sessionRepo.findById(sessionId)
                .map(Session::isActive)
                .orElse(false);
    }

    public List<SessionResponse> getActiveSessions() {
        User user = userService.getCurrentUser();
        List<Session> sessions = sessionRepo.findByUserAndActiveTrue(user);
        log.debug("Fetching active sessions for user {}", user.getEmail());
            return sessions
                .stream()
                .map(SessionMapper::toResponse)
                .toList();
    }

    public void deactivateAllSessionsForUser(User user) {
        List<Session> sessions = sessionRepo.findByUserAndActiveTrue(user);
        log.info("Deactivating all sessions for user {}", user.getEmail());
        sessions.forEach(session -> {
            session.setActive(false);
            List<RefreshToken> tokens = refreshTokenRepo.findBySessionAndActiveTrue(session);
            tokens.forEach(token -> token.setActive(false));
            if (!tokens.isEmpty()) refreshTokenRepo.saveAll(tokens);

        });
    }
}
