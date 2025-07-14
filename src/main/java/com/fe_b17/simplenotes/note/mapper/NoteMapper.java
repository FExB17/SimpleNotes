package com.fe_b17.simplenotes.note.mapper;

import com.fe_b17.simplenotes.note.dto.NoteRequest;
import com.fe_b17.simplenotes.note.dto.NoteResponse;
import com.fe_b17.simplenotes.note.models.Note;
import com.fe_b17.simplenotes.user.models.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NoteMapper {

    public NoteResponse toResponse(Note note){
        return new NoteResponse(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getTags() != null ? note.getTags() : List.of(),
                note.getCreatedAt(),
                note.getUpdatedAt()
        );
    }

    public List<NoteResponse> toResponseList(List<Note> notes){
        return notes.stream().map(this::toResponse).toList();
    }

    public Note toNote(NoteRequest noteRequest, Note note){
        note.setTitle(noteRequest.title());
        note.setContent(noteRequest.content());
        note.setTags(noteRequest.tags());
        return note;
    }

    public Note toNote(NoteRequest noteRequest, User user){
        Note note = new Note();
        note.setTitle(noteRequest.title());
        note.setContent(noteRequest.content());
        note.setTags(noteRequest.tags() != null ? noteRequest.tags(): new ArrayList<>());
        note.setUser(user);
        return note;
    }
}
