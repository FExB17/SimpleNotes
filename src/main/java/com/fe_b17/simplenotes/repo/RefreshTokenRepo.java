package com.fe_b17.simplenotes.repo;
import com.fe_b17.simplenotes.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByIdAndActiveTrue(UUID id);

    void deleteByActiveFalseAndExpiresAtBefore(Instant now);
}
