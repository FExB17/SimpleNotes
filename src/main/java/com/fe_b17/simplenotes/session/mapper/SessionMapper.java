package com.fe_b17.simplenotes.session.mapper;

import com.fe_b17.simplenotes.session.dto.SessionResponse;
import com.fe_b17.simplenotes.session.models.Session;
import org.springframework.stereotype.Component;

@Component
public class SessionMapper {


    public static SessionResponse toResponse(Session session) {
        return new SessionResponse(
                session.getId(),
                session.getUserAgent(),
                session.getIpAddress(),
                session.isActive(),
                session.getCreatedAt());
    }


}
