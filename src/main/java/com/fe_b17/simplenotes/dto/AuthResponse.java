package com.fe_b17.simplenotes.dto;

public record AuthResponse(String token,
                           long expiresAt,
                           UserDTO user)
{}