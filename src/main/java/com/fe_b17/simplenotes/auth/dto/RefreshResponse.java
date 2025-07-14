package com.fe_b17.simplenotes.auth.dto;

public record RefreshResponse(String accessToken, String refreshToken, Long expiresAt) {
}
