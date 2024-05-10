package com.ieltswise.exception;

public class EmailNotFoundException extends Exception {

    public EmailNotFoundException(String entity, String email) {
        super(String.format("%s with email %s not found", entity, email));
    }

    public EmailNotFoundException(String message) {
        super(message);
    }
}
