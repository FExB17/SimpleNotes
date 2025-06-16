package com.fe_b17.simplenotes.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super("E-Mail '" + email + "' is already registered.");
    }
}
