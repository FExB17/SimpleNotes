package com.fe_b17.simplenotes.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super("No user found with email: " + message);
    }
}
