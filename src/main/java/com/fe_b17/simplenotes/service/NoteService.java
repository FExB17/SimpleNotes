package com.fe_b17.simplenotes.service;

import com.fe_b17.simplenotes.dto.NoteRequest;
import com.fe_b17.simplenotes.exception.NoSuchNoteException;
import com.fe_b17.simplenotes.models.Note;
import com.fe_b17.simplenotes.repo.NoteRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class NoteService {

    private final NoteRepo noteRepo;


public Note createNote(NoteRequest noteRequest){
    Note note = new Note();
    note.setTitle(noteRequest.title());
    note.setContent(noteRequest.content());
    return noteRepo.save(note);
}


    public List<Note> getAllNotes() {
        return noteRepo.findAll();
    }

    public Note updateNote(NoteRequest dto, UUID id) {
        Note note = noteRepo.findById(id).orElseThrow(NoSuchElementException::new);
        note.setTitle(dto.title());
        note.setContent(dto.content());
        return noteRepo.save(note);
    }

    public void deleteNote(UUID id) {
    if(!noteRepo.existsById(id)){
        throw new NoSuchNoteException();
    }
        noteRepo.deleteById(id);
    }
}
