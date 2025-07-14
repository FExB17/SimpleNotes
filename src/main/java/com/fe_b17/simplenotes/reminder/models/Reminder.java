package com.fe_b17.simplenotes.reminder.models;


import com.fe_b17.simplenotes.note.models.Note;
import com.fe_b17.simplenotes.user.models.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

@Getter @Setter
@Entity
@Table(name = "reminders")
public class Reminder {

    @Id
    @GeneratedValue
    private UUID id;

    private String text;

    @Column(nullable = false)
    private Instant triggerAtUtc;
    private boolean useUtc = true;

    private LocalTime triggerAtLocalTime;
    private String zoneId;

    private boolean allDay;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RepeatMode repeatMode = RepeatMode.NONE;

    @Column(nullable = false)
    private boolean active = true;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Note note;

}