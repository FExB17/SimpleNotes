package com.fe_b17.simplenotes.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("Invalid or expired token.");
    }
}
