package com.fe_b17.simplenotes.mapper;

import com.fe_b17.simplenotes.ZoneContext;
import com.fe_b17.simplenotes.dto.ReminderResponse;
import com.fe_b17.simplenotes.models.Reminder;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class ReminderMapper {

    public static ReminderResponse toResponse(Reminder reminder) {
        ZoneId zone = ZoneContext.get();
        return  new ReminderResponse(
          reminder.getId(),
          reminder.getTriggerAt().atZone(zone),
          reminder.isAllDay(),
          reminder.getRepeatMode().name(),
          reminder.getText(),
          reminder.getNote().getId()
        );
    }

}
