package com.gianmarques.supporticket.exception.exceptions;

public class TicketAlreadyClosedException extends RuntimeException {

    public TicketAlreadyClosedException(String message) {
        super(message);
    }
}
