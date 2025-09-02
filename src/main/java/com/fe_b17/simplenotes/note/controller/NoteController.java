package com.fe_b17.simplenotes.note.controller;

import com.fe_b17.simplenotes.note.dto.*;
import com.fe_b17.simplenotes.note.mapper.NoteMapper;
import com.fe_b17.simplenotes.note.models.Note;
import com.fe_b17.simplenotes.note.service.NoteService;
import com.fe_b17.simplenotes.user.models.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;
    private final NoteMapper noteMapper;

    @PostMapping
    public ResponseEntity<NoteResponse> addNote(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody NoteRequest dto) {

        Note note = noteService.createNote(principal, dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(noteMapper.toResponse(note));
    }

    @GetMapping
    public ResponseEntity<List<NoteResponse>> getAllNotes(
            @AuthenticationPrincipal UserPrincipal principal) {

        List<Note> notes = noteService.getAllNotes(principal);
        return ResponseEntity.ok(noteMapper.toResponseList(notes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> getNote(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id) {

        Note note = noteService.getNote(principal, id);
        return ResponseEntity.ok(noteMapper.toResponse(note));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponse> updateNote(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody NoteRequest dto,
            @PathVariable UUID id) {

        Note updated = noteService.updateNote(principal, dto, id);
        return ResponseEntity.ok(noteMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID id) {

        noteService.deleteNote(principal, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<NoteResponse>> searchNotes(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam String q) {

        List<Note> notes = noteService.searchNotes(principal, q);
        return ResponseEntity.ok(noteMapper.toResponseList(notes));
    }
}
