package com.fe_b17.simplenotes.controller;

import com.fe_b17.simplenotes.dto.ReminderRequest;
import com.fe_b17.simplenotes.dto.ReminderResponse;
import com.fe_b17.simplenotes.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;

    @PostMapping
    public ResponseEntity<Void> createReminder(@RequestBody ReminderRequest dto) {
        reminderService.createReminder(dto);
        return ResponseEntity.ok().build();
    }
    @GetMapping
    public ResponseEntity<List<ReminderResponse>> getAllReminders() {
        return ResponseEntity.ok(reminderService.getAllReminders());
    }


}
