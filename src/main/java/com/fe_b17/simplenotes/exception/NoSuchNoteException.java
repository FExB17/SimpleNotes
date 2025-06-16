package com.fe_b17.simplenotes.exception;

public class NoSuchNoteException extends RuntimeException {

    public NoSuchNoteException() {
        super("No such note");
    }
}
