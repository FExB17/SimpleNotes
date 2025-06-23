package com.fe_b17.simplenotes.mapper;

import com.fe_b17.simplenotes.dto.ReminderResponse;
import com.fe_b17.simplenotes.models.Reminder;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class ReminderMapper {

    public ReminderResponse toResponse(Reminder reminder, ZoneId zoneId) {
        return  new ReminderResponse(
          reminder.getId(),
          reminder.getTriggerAt().atZone(zoneId),
          reminder.isAllDay(),
          reminder.getRepeatMode().name(),
          reminder.getText(),
          reminder.getNote().getId()
        );
    }

}
