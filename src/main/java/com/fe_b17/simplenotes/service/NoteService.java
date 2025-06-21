package com.fe_b17.simplenotes.service;

import com.fe_b17.simplenotes.dto.NoteRequest;
import com.fe_b17.simplenotes.exception.NoSuchNoteException;
import com.fe_b17.simplenotes.mapper.NoteMapper;
import com.fe_b17.simplenotes.models.Note;
import com.fe_b17.simplenotes.models.User;
import com.fe_b17.simplenotes.repo.NoteRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


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
        User currentUser = userService.getCurrentUser();
        Note note = noteRepo.findById(id)
                .filter(n -> n.getUser().getId().equals(currentUser.getId()))
                .orElseThrow(NoSuchNoteException::new);

        note = noteMapper.toNote(noteRequest, note);

        return noteRepo.save(note); // gibt die note inklusive dem user und allen Daten mit passwort wieder
    }

    public void deleteNote(UUID id) {
        User currentUser = userService.getCurrentUser();
        Note note = noteRepo.findById(id)
                .filter(n -> n.getUser().getId().equals(currentUser.getId()))
                .orElseThrow(NoSuchNoteException::new);
        noteRepo.delete(note);
    }

    public List<Note> getAllNotes(){
    UUID userID = userService.getCurrentUser().getId();
    return noteRepo.findAllByUserId(userID);
}

    public Note getNote(UUID id) {
        User currentUser = userService.getCurrentUser();
         return noteRepo.findById(id)
                 .filter(n -> n.getUser().getId().equals(currentUser.getId()))
                 .orElseThrow(NoSuchNoteException::new);
    }

    public List<Note> searchNotes(String q) {
        UUID userID = userService.getCurrentUser().getId();
        return noteRepo.searchByTitleOrContent(q, userID);
    }
}
