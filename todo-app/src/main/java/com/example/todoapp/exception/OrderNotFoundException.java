package com.example.todoapp.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id){
        super("Could not found order with id " + id);
    }
}
