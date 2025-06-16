package com.fe_b17.simplenotes.controller;

import com.fe_b17.simplenotes.dto.NoteRequest;
import com.fe_b17.simplenotes.dto.NoteResponse;
import com.fe_b17.simplenotes.models.Note;
import com.fe_b17.simplenotes.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<NoteResponse> addNote(@Valid @RequestBody NoteRequest dto){
        Note note = noteService.createNote(dto);
        NoteResponse noteResponse = new NoteResponse(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getCreatedAt());
        return ResponseEntity.status(HttpStatus.CREATED).body(noteResponse);
    }

    @GetMapping
    public ResponseEntity<List<NoteResponse>> getNotes(){
        List<Note> notes = noteService.getAllNotes();
        List<NoteResponse> dto = notes.stream()
                .map(note -> new NoteResponse(
                        note.getId(),
                        note.getTitle(),
                        note.getContent(),
                        note.getCreatedAt()))
                .toList();
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@RequestBody NoteRequest dto, @PathVariable UUID id){
        Note updated = noteService.updateNote(dto,id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable UUID id){
        noteService.deleteNote(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
