package com.fe_b17.simplenotes.repo;

import com.fe_b17.simplenotes.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {


    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
