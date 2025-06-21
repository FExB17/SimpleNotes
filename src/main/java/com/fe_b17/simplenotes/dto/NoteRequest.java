package com.fe_b17.simplenotes.dto;


import jakarta.validation.constraints.Size;

public record NoteRequest(

        @Size(max=500)
        String title,

        String content
) {}
