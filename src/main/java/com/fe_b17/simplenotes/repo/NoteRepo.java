package com.fe_b17.simplenotes.repo;

import com.fe_b17.simplenotes.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface NoteRepo extends JpaRepository<Note, UUID> {

    List<Note> findAllByUserId(UUID userId);

    @Query("SELECT n FROM Note n WHERE n.user.id = :userId AND " +
            "(LOWER(n.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(n.content) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Note> searchByTitleOrContent(@Param("query") String q, @Param("userId") UUID userId);
}
