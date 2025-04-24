package com.lazywallet.lazywallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(new ErrorResponse("Validation failed", errors, HttpStatus.BAD_REQUEST.value()));
    }
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientFunds(InsufficientFundsException ex) {
        ErrorResponse response = new ErrorResponse(
                ex.getMessage(),
                List.of(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.badRequest().body(response);
    }
    // Обработка ошибки уже существующего email
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        ErrorResponse response = new ErrorResponse(
                ex.getMessage(),
                List.of(), // список деталей пустой
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.badRequest().body(response);
    }
}