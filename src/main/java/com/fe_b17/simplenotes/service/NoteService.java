package com.fe_b17.simplenotes.service;

import com.fe_b17.simplenotes.dto.NoteRequest;
import com.fe_b17.simplenotes.exception.NoSuchNoteException;
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

    public Note createNote(NoteRequest noteRequest){
    User currentUser = userService.getCurrentUser();
    Note note = new Note();
    note.setTitle(noteRequest.title());
    note.setContent(noteRequest.content());
    note.setUser(currentUser);
    return noteRepo.save(note);

}

    public Note updateNote(NoteRequest dto, UUID id) {
        User currentUser = userService.getCurrentUser();
        Note note = noteRepo.findById(id)
                .filter(n -> n.getUser().getId().equals(currentUser.getId()))
                .orElseThrow(NoSuchNoteException::new);

        note.setTitle(dto.title());
        note.setContent(dto.content());
        return noteRepo.save(note); // gibt die note inklusive dem user und allen Daten mit passwort wieder
    }

    public void deleteNote(UUID id) {
        User currentUser = userService.getCurrentUser();
        Note note = noteRepo.findById(id)
                .filter(n -> n.getUser().getId().equals(currentUser.getId()))
                .orElseThrow(NoSuchNoteException::new);
        noteRepo.delete(note);
    }

    public List<Note> getNotesForCurrentUser(){
    UUID userID = userService.getCurrentUser().getId();
    return noteRepo.findByUserId(userID);
}

    public Note getNote(UUID id) {
        User currentUser = userService.getCurrentUser();
         return noteRepo.findById(id)
                 .filter(n -> n.getUser().getId().equals(currentUser.getId()))
                 .orElseThrow(NoSuchNoteException::new);
    }
}
