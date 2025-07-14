package com.fe_b17.simplenotes.note.dto;


import jakarta.validation.constraints.Size;

import java.util.List;

public record NoteRequest(

        @Size(max=500)
        String title,

        String content,

        List<String> tags
) {}
