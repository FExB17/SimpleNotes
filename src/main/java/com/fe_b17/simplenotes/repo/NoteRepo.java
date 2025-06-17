package com.fe_b17.simplenotes.repo;

import com.fe_b17.simplenotes.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NoteRepo extends JpaRepository<Note, UUID> {

    List<Note> findByUserId(UUID userId);
}
