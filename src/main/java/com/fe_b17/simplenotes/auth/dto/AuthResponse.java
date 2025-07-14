package com.fe_b17.simplenotes.auth.dto;

import com.fe_b17.simplenotes.user.dto.UserResponse;

public record AuthResponse(String accessToken,
                           String refreshToken,
                           Long expiresAt,
                           UserResponse user)
{}