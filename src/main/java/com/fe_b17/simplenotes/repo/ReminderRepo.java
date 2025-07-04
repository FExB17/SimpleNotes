package com.fe_b17.simplenotes.repo;

import com.fe_b17.simplenotes.models.Reminder;
import com.fe_b17.simplenotes.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReminderRepo extends JpaRepository<Reminder, UUID> {

    @Modifying
    void deleteByUser(User user);

    List<Reminder> findAllByUserId(UUID userId);

    List<Reminder> findAllByActiveTrue();
}
