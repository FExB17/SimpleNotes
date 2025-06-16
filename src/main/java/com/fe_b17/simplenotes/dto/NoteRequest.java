package com.fe_b17.simplenotes.dto;


import java.util.UUID;

public record NoteRequest(

        UUID id,

        String title,

        String content

) {
}
