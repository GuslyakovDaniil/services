package org.example.todoapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;


@ControllerAdvice
public class ExceptionHandlers {

    private static final String ERROR_STATUS = "error";

    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<StatusResponse> handleBadRequestException(InvalidArgumentException e) {
        var status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(new StatusResponse(ERROR_STATUS, e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StatusResponse> handleNotFoundException(NotFoundException e){
        var status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(new StatusResponse(ERROR_STATUS,e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StatusResponse> handleValidationException(MethodArgumentNotValidException e){
        var status = HttpStatus.BAD_REQUEST;
        var errors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(status).body(new StatusResponse(ERROR_STATUS, "Validation error: " + errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StatusResponse> handleInternalServerError(Exception e){
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(new StatusResponse(ERROR_STATUS, "Internal server error: " + e.getMessage()));
    }


}