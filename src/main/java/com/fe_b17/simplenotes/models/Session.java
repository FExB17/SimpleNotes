package com.fe_b17.simplenotes.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;


@Getter @Setter
@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    private String userAgent;
    private String ipAddress;

    private boolean active;

    @CreationTimestamp
    private Instant createdAt;

}
