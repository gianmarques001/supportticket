package com.gianmarques.supporttracker.exception.exceptions;

public class TicketAlreadyClosedException extends RuntimeException {

    public TicketAlreadyClosedException(String message) {
        super(message);
    }
}
