package com.ieltswise.exception;

public class BookingSessionException extends Exception{

    public BookingSessionException() {
        super("Failed to book lesson");
    }

    public BookingSessionException(String message) {
        super(message);
    }
}
