package com.fe_b17.simplenotes.mapper;

import com.fe_b17.simplenotes.dto.SessionResponse;
import com.fe_b17.simplenotes.models.Session;

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
