package com.fe_b17.simplenotes.session.models;


import com.fe_b17.simplenotes.user.models.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;
import java.util.UUID;

@Getter @Setter
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue
    private UUID id; // the id is the refresh token itself

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private Session session;

    @CreationTimestamp
    private Instant createdAt;

    private Instant expiresAt;

    private boolean active = true;

}
