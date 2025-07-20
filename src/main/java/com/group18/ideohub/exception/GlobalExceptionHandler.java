package com.group18.ideohub.exception;

import com.group18.ideohub.response.RegisterResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<RegisterResponse<String>> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        RegisterResponse<String> response = new RegisterResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<RegisterResponse<String>> handleBadRequestException(BadRequestException ex, WebRequest request) {
        RegisterResponse<String> response = new RegisterResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RegisterResponse<String>> handleGlobalException(Exception ex, WebRequest request) {
        RegisterResponse<String> response = new RegisterResponse<>(false, "An internal error occurred: " + ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}