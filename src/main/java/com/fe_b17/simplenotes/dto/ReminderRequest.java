package com.fe_b17.simplenotes.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

public record ReminderRequest(ZonedDateTime triggerAt,
                              boolean allDay,
                              String repeatMode,
                              String text,
                              UUID noteId) {
}
