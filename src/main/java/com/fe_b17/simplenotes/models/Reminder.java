package com.fe_b17.simplenotes.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter
@Entity
@Table(name = "reminders")
public class Reminder {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private Instant triggerAt;

    private boolean allDay;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RepeatMode repeatMode = RepeatMode.NONE;

    private String text;

    @Column(nullable = false)
    private boolean active = true;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Note note;
}

