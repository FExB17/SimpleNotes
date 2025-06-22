package com.fe_b17.simplenotes.service;

import com.fe_b17.simplenotes.models.Session;
import com.fe_b17.simplenotes.models.User;
import com.fe_b17.simplenotes.repo.SessionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepo sessionRepo;

    public Session createSession(User user, String ipAddress, String userAgent) {
        Session session = new Session();
        session.setUser(user);
        session.setIpAddress(ipAddress);
        session.setUserAgent(userAgent);
        session.setActive(true);
        return sessionRepo.save(session);
    }

    public void deactivateSession(UUID sessionId) {

        sessionRepo.findById(sessionId).ifPresent(session -> {
            session.setActive(false);
            sessionRepo.save(session);
        });
    }

    public boolean isActive(UUID sessionId){
        return sessionRepo.findById(sessionId)
                .map(Session::isActive)
                .orElse(false);
    }

    public List<Session> getActiveSessions(User user){
        return sessionRepo.findByUserAndActiveTrue(user);
    }

    public void deactivateAllSessionsForUser(User user){
        List<Session> sessions = sessionRepo.findByUserAndActiveTrue(user);
        sessions.forEach(session -> session.setActive(false));
        sessionRepo.saveAll(sessions);
    }

}
