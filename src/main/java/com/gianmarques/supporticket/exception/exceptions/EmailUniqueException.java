package com.gianmarques.supporticket.exception.exceptions;

public class EmailUniqueException extends RuntimeException {
    public EmailUniqueException(String message) {
        super(message);
    }
}
