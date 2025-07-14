package com.fe_b17.simplenotes.user.dto;

import java.time.LocalDateTime;

public record UserResponse(String email,
                           String username,
                           String role,
                           LocalDateTime createdAt) {
}
