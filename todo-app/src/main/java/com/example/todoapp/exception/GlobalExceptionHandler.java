package com.example.todoapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class, ProductNotFoundException.class, OrderNotFoundException.class, CategoryNotFoundException.class})
    public ResponseEntity<?> notFoundException(RuntimeException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
