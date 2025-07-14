package com.fe_b17.simplenotes.reminder.service;


import com.fe_b17.simplenotes.reminder.dto.ReminderRequest;
import com.fe_b17.simplenotes.reminder.dto.ReminderResponse;
import com.fe_b17.simplenotes.exception.InvalidRequestException;
import com.fe_b17.simplenotes.exception.NoSuchNoteException;
import com.fe_b17.simplenotes.exception.NotePermissionException;
import com.fe_b17.simplenotes.reminder.mapper.ReminderMapper;
import com.fe_b17.simplenotes.note.models.Note;
import com.fe_b17.simplenotes.reminder.models.Reminder;
import com.fe_b17.simplenotes.reminder.models.RepeatMode;
import com.fe_b17.simplenotes.user.models.User;
import com.fe_b17.simplenotes.note.repo.NoteRepo;
import com.fe_b17.simplenotes.reminder.repo.ReminderRepo;
import com.fe_b17.simplenotes.user.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderService {

    private final ReminderRepo reminderRepo;
    private final NoteRepo noteRepo;
    private final UserService userService;

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

    @PostConstruct
    public void init() {
        reminderRepo.findAllByActiveTrue().forEach(this::schedule);
    }

    public void createReminder(ReminderRequest dto) {
        User currentUser = userService.getCurrentUser();

        Note note = null;
        if (dto.noteId() != null) {
            note = noteRepo.findById(dto.noteId())
                    .orElseThrow(NoSuchNoteException::new);

            if (!note.getUser().getId().equals(currentUser.getId())) {
                throw new NotePermissionException();
            }
        }

        Reminder r = new Reminder();
        r.setText(dto.text());
        r.setRepeatMode(RepeatMode.valueOf(dto.repeatMode()));
        r.setAllDay(dto.allDay());
        r.setUseUtc(dto.useUtc());
        r.setNote(note);
        r.setUser(currentUser);

        if (dto.useUtc()) {
            r.setTriggerAtUtc(dto.triggerAt().toInstant());
        } else {
            ZoneId zone = ZoneId.of(dto.timeZone());
            LocalTime localTime = LocalTime.parse(dto.localTime());
            ZonedDateTime zoned = ZonedDateTime.of(LocalDate.now(zone), localTime, zone);
            r.setTriggerAtUtc(zoned.toInstant());
            r.setZoneId(zone.getId());
            r.setTriggerAtLocalTime(localTime);
        }

        try {
            Reminder saved = reminderRepo.save(r);
            schedule(saved);
        } catch (Exception e) {
            log.error("Failed to save reminder {}", e.getMessage(),e);
            throw new InvalidRequestException("Could not save reminder");
        }
    }

    public void triggerReminder(Reminder r) {
        log.info("Triggering reminder {}", r.getText());

        Instant next = r.getRepeatMode().getNextTrigger(Instant.now());
        if (next == null) {
            r.setActive(false);
        } else {
            r.setTriggerAtUtc(next);
            schedule(r);
        }

        reminderRepo.save(r);
    }

    private void schedule(Reminder r) {
        Instant now = Instant.now();
        Instant trigger = r.getTriggerAtUtc();

        if (trigger.isBefore(now)) return;

        long delay = Duration.between(now, trigger).toMillis();
        executor.schedule(() -> triggerReminder(r), delay, TimeUnit.MILLISECONDS);
    }

    public List<ReminderResponse> getAllReminders() {
        User user = userService.getCurrentUser();
        return reminderRepo.findAllByUserId(user.getId()).stream()
                .map(ReminderMapper::toResponse)
                .toList();
    }
}
