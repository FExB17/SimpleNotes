package com.fe_b17.simplenotes.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank
        String email,

        @NotBlank
        String password) {
}
