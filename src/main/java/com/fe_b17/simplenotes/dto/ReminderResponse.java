package com.fe_b17.simplenotes.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

public record ReminderResponse (
        UUID id,
        ZonedDateTime triggerAt,
        boolean allDay,
        String repeatMode,
        String text,
        UUID noteId

)     {}
