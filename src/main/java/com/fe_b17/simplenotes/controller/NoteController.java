package com.fe_b17.simplenotes.controller;

import com.fe_b17.simplenotes.dto.NoteRequest;
import com.fe_b17.simplenotes.dto.NoteResponse;
import com.fe_b17.simplenotes.mapper.NoteMapper;
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
    private final NoteMapper noteMapper;

    @PostMapping
    public ResponseEntity<NoteResponse> addNote(@Valid @RequestBody NoteRequest dto){
        Note note = noteService.createNote(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(noteMapper.toResponse(note));
    }

    @GetMapping
    public ResponseEntity<List<NoteResponse>> getAllNotes(){
        List<Note> notes = noteService.getAllNotes();
        return ResponseEntity.ok(noteMapper.toResponseList(notes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> getNote(@PathVariable UUID id){
        Note note = noteService.getNote(id);
        return ResponseEntity.ok( noteMapper.toResponse(note));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponse> updateNote(@RequestBody NoteRequest dto, @PathVariable UUID id){
        Note updated = noteService.updateNote(dto,id);
        return ResponseEntity.ok(noteMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable UUID id){
        noteService.deleteNote(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<NoteResponse>> searchNotes(@RequestParam String q){
        List<Note> notes = noteService.searchNotes(q);
        return ResponseEntity.ok(noteMapper.toResponseList(notes));
    }

}
