package com.slay.course.exception;

public class InvalidJsonException extends RuntimeException {
    public InvalidJsonException(String message) {
        super(message);
    }
}