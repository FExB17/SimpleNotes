package com.fe_b17.simplenotes.dto;

import org.springframework.http.HttpStatus;

import java.util.Map;

public record ValidationErrorResponse(
        int status,
        String message,
        String path,
        Map<String, String> errors
) {
    public static ValidationErrorResponse of(HttpStatus status, String message, String path, Map<String, String> errors) {
        return new ValidationErrorResponse(status.value(), message, path, errors);
    }
}
