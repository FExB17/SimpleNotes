package com.fe_b17.simplenotes.mapper;

import com.fe_b17.simplenotes.dto.NoteResponse;
import com.fe_b17.simplenotes.models.Note;
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
}
