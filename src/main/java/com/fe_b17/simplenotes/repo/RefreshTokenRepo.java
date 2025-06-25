package com.fe_b17.simplenotes.repo;
import com.fe_b17.simplenotes.models.RefreshToken;
import com.fe_b17.simplenotes.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByIdAndActiveTrue(UUID id);

    @Modifying
    void deleteByActiveFalseAndExpiresAtBefore(Instant now);

    @Modifying
    void deleteByUser(User user);
}
