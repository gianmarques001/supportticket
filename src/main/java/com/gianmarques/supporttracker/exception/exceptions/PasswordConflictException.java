package com.gianmarques.supporttracker.exception.exceptions;

public class PasswordConflictException extends RuntimeException {
    public PasswordConflictException(String message) {
        super(message);
    }
}
