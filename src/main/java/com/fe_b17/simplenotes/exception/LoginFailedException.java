package com.fe_b17.simplenotes.exception;

public class LoginFailedException extends RuntimeException {
    public LoginFailedException() {
        super("Login failed: Invalid email or password");
    }

}
