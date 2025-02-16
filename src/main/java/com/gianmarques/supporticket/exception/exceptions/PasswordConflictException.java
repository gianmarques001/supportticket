package com.gianmarques.supporticket.exception.exceptions;

public class PasswordConflictException extends RuntimeException {
    public PasswordConflictException(String message) {
        super(message);
    }
}
