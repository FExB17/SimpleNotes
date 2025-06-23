package com.fe_b17.simplenotes.repo;

import com.fe_b17.simplenotes.models.Session;
import com.fe_b17.simplenotes.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SessionRepo extends JpaRepository<Session, UUID> {

    List<Session> findByUserAndActiveTrue(User user);
}
