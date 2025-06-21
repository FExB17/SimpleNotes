package com.fe_b17.simplenotes.mapper;

import com.fe_b17.simplenotes.dto.NoteRequest;
import com.fe_b17.simplenotes.dto.NoteResponse;
import com.fe_b17.simplenotes.models.Note;
import com.fe_b17.simplenotes.models.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NoteMapper {

    public NoteResponse toResponse(Note note){
        return new NoteResponse(note.getId(),note.getTitle(),note.getContent(),note.getCreatedAt());
    }

    public List<NoteResponse> toResponseList(List<Note> notes){
        return notes.stream().map(this::toResponse).toList();
    }

    public Note toNote(NoteRequest noteRequest, Note note){
        note.setTitle(noteRequest.title());
        note.setContent(noteRequest.content());
        return note;
    }

    public Note toNote(NoteRequest noteRequest, User user){
        Note note = new Note();
        note.setTitle(noteRequest.title());
        note.setContent(noteRequest.content());
        note.setUser(user);
        return note;
    }
}
