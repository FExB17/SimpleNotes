package com.fe_b17.simplenotes.note.service;

import com.fe_b17.simplenotes.note.dto.NoteRequest;
import com.fe_b17.simplenotes.exception.*;
import com.fe_b17.simplenotes.note.mapper.NoteMapper;
import com.fe_b17.simplenotes.note.models.Note;
import com.fe_b17.simplenotes.note.repo.NoteRepo;
import com.fe_b17.simplenotes.user.models.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepo noteRepo;
    private final NoteMapper noteMapper;

    public Note createNote(UserPrincipal principal, NoteRequest dto) {
        Note note = noteMapper.toNote(dto, principal.user());
        return noteRepo.save(note);
    }

    public List<Note> getAllNotes(UserPrincipal principal) {
        return noteRepo.findAllByUserId(principal.getId());
    }

    public Note getNote(UserPrincipal principal, UUID id) {
        return getNoteForUser(principal, id);
    }


    public List<Note> searchNotes(UserPrincipal principal, String query) {
        return noteRepo.searchByTitleOrContent(query, principal.getId());
    }

    public Note updateNote(UserPrincipal principal, NoteRequest dto, UUID id) {
        Note note = getNoteForUser(principal, id);
        noteMapper.toNote(dto, note);   // map in place
        log.info("Note updated by {} (noteId={})", principal.getUsername(), id);
        return noteRepo.save(note);
    }

    public void deleteNote(UserPrincipal principal, UUID id) {
        Note note = getNoteForUser(principal, id);
        noteRepo.delete(note);
        log.info("Note deleted by {} (noteId={})", principal.getUsername(), id);
    }

    private Note getNoteForUser(UserPrincipal principal, UUID id) {
        Note note = noteRepo.findById(id)
                .orElseThrow(NoSuchNoteException::new);

        if (!note.getUser().getId().equals(principal.getId())) {
            log.warn("Unauthorized access by {} to note {}", principal.getUsername(), id);
            throw new NotePermissionException();
        }
        return note;
    }
}
