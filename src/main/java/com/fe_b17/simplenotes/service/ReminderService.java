package com.fe_b17.simplenotes.service;


import com.fe_b17.simplenotes.dto.ReminderRequest;
import com.fe_b17.simplenotes.dto.ReminderResponse;
import com.fe_b17.simplenotes.mapper.ReminderMapper;
import com.fe_b17.simplenotes.models.Note;
import com.fe_b17.simplenotes.models.Reminder;
import com.fe_b17.simplenotes.models.RepeatMode;
import com.fe_b17.simplenotes.models.User;
import com.fe_b17.simplenotes.repo.NoteRepo;
import com.fe_b17.simplenotes.repo.ReminderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final ReminderRepo reminderRepo;
    private final NoteRepo noteRepo;
    private final UserService userService;
    private final ReminderMapper reminderMapper;

    public void createReminder(ReminderRequest dto) {
        User currentUser = userService.getCurrentUser();

        Note note = noteRepo.findById(dto.noteId())
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        ZonedDateTime zonedTrigger = dto.allDay()
                ? dto.triggerAt().with(LocalTime.MIDNIGHT)
                : dto.triggerAt();

        Instant triggerAt = zonedTrigger.toInstant();

        Reminder reminder = new Reminder();
        reminder.setTriggerAt(triggerAt);
        reminder.setRepeatMode(RepeatMode.valueOf(dto.repeatMode()));
        reminder.setText(dto.text());
        reminder.setUser(currentUser);
        reminder.setNote(note);

        reminderRepo.save(reminder);
    }

    public List<ReminderResponse> getAllReminders() {
        User user = userService.getCurrentUser();
        List<Reminder> reminders = reminderRepo.findAllByUserId(user.getId());
        return null;

    }
}

