package com.fe_b17.simplenotes.repo;

import com.fe_b17.simplenotes.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NoteRepo extends JpaRepository<Note, UUID> {


}
