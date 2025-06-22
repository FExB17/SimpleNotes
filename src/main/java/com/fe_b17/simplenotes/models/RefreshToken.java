package com.fe_b17.simplenotes.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Session session;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant expiresAt;

    private boolean active = true;

}
