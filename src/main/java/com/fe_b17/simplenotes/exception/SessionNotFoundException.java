package com.fe_b17.simplenotes.exception;

import java.util.UUID;

public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException(UUID id) {
        super("Session not found: " + id);
    }
}
