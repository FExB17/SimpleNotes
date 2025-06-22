package com.fe_b17.simplenotes.dto;

public record AuthResponse(String token,
                           Long expiresAt,
                           UserDTO user)
{}