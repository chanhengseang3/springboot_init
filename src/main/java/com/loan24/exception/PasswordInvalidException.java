package com.loan24.exception;

public class PasswordInvalidException extends RuntimeException {

    public PasswordInvalidException() {
        super("Old password is invalid");
    }
}
