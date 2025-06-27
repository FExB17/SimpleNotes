package com.fe_b17.simplenotes.exception;

public class NotePermissionException extends RuntimeException {
    public NotePermissionException() {
        super("You are not authorized to access this note.");
    }
}
