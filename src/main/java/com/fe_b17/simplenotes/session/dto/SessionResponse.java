package com.fe_b17.simplenotes.session.dto;

import java.time.Instant;
import java.util.UUID;

public record SessionResponse(
        UUID id,
        String userAgent,
        String ipAddress,
        boolean active,
        Instant createdAt
) {
}
