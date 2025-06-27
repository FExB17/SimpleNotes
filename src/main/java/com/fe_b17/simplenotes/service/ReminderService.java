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
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
                    .orElseThrow(() -> new RuntimeException("Note not found"));

            if (!note.getUser().getId().equals(currentUser.getId())) {
                throw new RuntimeException("Unauthorized");
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
            throw new RuntimeException("Could not save Reminder: " + e);
        }
    }

    public void triggerReminder(Reminder r) {
        System.out.println("Reminder triggered: " + r.getText());

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
