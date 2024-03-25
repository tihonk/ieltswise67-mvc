package com.ieltswise.exception;

public class TutorCreationException extends Exception {

    public TutorCreationException() {
        super("Failed to create tutor");
    }

    public TutorCreationException(String message) {
        super(message);
    }
}
