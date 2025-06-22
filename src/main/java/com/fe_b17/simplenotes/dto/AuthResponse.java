package com.fe_b17.simplenotes.dto;

public record AuthResponse(String accessToken,
                           String refreshToken,
                           Long expiresAt,
                           UserDTO user)
{}