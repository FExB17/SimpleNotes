package com.fe_b17.simplenotes.dto;

public record RefreshRequest(
        String refreshId,
        String timeZone // wird für den access token benötigt
) {
}
