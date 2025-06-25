package com.fe_b17.simplenotes.mapper;

import com.fe_b17.simplenotes.dto.ReminderResponse;
import com.fe_b17.simplenotes.models.Reminder;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class ReminderMapper {

    public static ReminderResponse toResponse(Reminder reminder) {
        return  new ReminderResponse(
          reminder.getId(),
          reminder.getTriggerAtUtc().atZone(ZoneOffset.UTC),
          reminder.isUseUtc(),
          reminder.getZoneId(),
          reminder.getTriggerAtLocalTime() != null ? reminder.getTriggerAtLocalTime().toString() : null,
          reminder.isAllDay(),
          reminder.getRepeatMode().name(),
          reminder.getText(),
          reminder.getNote().getId()
        );
    }

}
