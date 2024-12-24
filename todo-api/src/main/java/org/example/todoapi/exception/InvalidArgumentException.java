package org.example.todoapi.exception;

public class InvalidArgumentException extends RuntimeException {

    public InvalidArgumentException(String message) {
        super("invalid request: " + message);
    }

}