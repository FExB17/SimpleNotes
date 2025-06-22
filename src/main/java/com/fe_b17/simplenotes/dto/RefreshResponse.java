package com.fe_b17.simplenotes.dto;

public record RefreshResponse(String accessToken, String refreshToken, Long expiresAt) {
}
