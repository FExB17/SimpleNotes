package com.fe_b17.simplenotes.note.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record NoteResponse(
        UUID id,
        String title,
        String content,
        List<String> tags,
        Instant createdAt,
        Instant updatedAt
) {
}
