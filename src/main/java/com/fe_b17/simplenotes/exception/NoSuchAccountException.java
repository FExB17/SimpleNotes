package com.fe_b17.simplenotes.exception;

public class NoSuchAccountException extends RuntimeException {

    public NoSuchAccountException() {
        super("No such account");
    }
}
