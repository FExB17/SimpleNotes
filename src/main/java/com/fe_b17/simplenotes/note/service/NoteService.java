package com.fe_b17.simplenotes.note.service;

import com.fe_b17.simplenotes.note.dto.NoteRequest;
import com.fe_b17.simplenotes.exception.NoSuchNoteException;
import com.fe_b17.simplenotes.exception.NotePermissionException;
import com.fe_b17.simplenotes.note.mapper.NoteMapper;
import com.fe_b17.simplenotes.note.models.Note;
import com.fe_b17.simplenotes.user.models.User;
import com.fe_b17.simplenotes.note.repo.NoteRepo;
import com.fe_b17.simplenotes.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@Service
public class NoteService {

    private final NoteRepo noteRepo;
    private final UserService userService;
    private final NoteMapper noteMapper;

    public Note createNote(NoteRequest noteRequest){
    User currentUser = userService.getCurrentUser();
    Note note = noteMapper.toNote(noteRequest,currentUser);
    return noteRepo.save(note);
}

    public Note updateNote(NoteRequest noteRequest, UUID id) {
        Note note = getNoteForCurrentUser(id);
        note = noteMapper.toNote(noteRequest, note);
        log.info("Note updated by user {} with ID {}", note.getUser().getEmail(), note.getId());
        return noteRepo.save(note);
    }

    public void deleteNote(UUID id) {
        Note note = getNoteForCurrentUser(id);
        noteRepo.delete(note);
        log.info("Note deleted by user {} with ID {}", note.getUser().getEmail(), note.getId());
    }

    public List<Note> getAllNotes(){
    UUID userID = userService.getCurrentUser().getId();
    return noteRepo.findAllByUserId(userID);
}

    public Note getNote(UUID id) {
        return getNoteForCurrentUser(id);
    }

    public List<Note> searchNotes(String q) {
        UUID userID = userService.getCurrentUser().getId();
        return noteRepo.searchByTitleOrContent(q, userID);
    }

    public Note getNoteForCurrentUser(UUID id){
        Note note = noteRepo.findById(id)
                .orElseThrow(NoSuchNoteException::new);
        User user = userService.getCurrentUser();
        if(!note.getUser().getId().equals(user.getId())){
            log.warn("Unauthorized access attempt by user {} to note {}", user.getEmail(), id);
            throw new NotePermissionException();
        }
        return note;
    }
}
