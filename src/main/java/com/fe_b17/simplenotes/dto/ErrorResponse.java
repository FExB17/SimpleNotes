package com.fe_b17.simplenotes.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponse (
        int status,
        String message,
        String path,
        LocalDateTime timeStamp
) {
    public static ErrorResponse of (HttpStatus status, String message, String path) {
        return new ErrorResponse(status.value(), message, path, LocalDateTime.now());
    }
}
