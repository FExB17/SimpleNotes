package com.fe_b17.simplenotes.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record NoteRequest(

        @NotEmpty
        UUID id,
        @Size(max=500)
        String title,

        String content
) {}
