package com.fe_b17.simplenotes.reminder.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

public record ReminderRequest(

      boolean useUtc,
      // used only if useUtc is true (for global meetings)
      ZonedDateTime triggerAt,
      // used with timeZone and only if useUtc is false(for daily routines)
      String localTime,
      String timeZone,
      boolean allDay,
      String repeatMode,
      String text,
      UUID noteId
) {
}
