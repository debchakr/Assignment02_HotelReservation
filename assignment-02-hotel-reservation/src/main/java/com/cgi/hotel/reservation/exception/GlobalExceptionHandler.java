package com.cgi.hotel.reservation.exception;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Credit Card Errors
    @ExceptionHandler(CreditCardServiceException.class)
    public ResponseEntity<ErrorResponse> handleCreditCardException(
        CreditCardServiceException ex) {

        log.error("Credit card service error: {}", ex.getMessage());

        HttpStatus status = HttpStatus.BAD_GATEWAY;

        ErrorResponse errorResponse = ErrorResponse.builder()
            .message(ex.getMessage())
            .status(status.value())
            .timestamp(LocalDateTime.now())
            .build();

        return new ResponseEntity<>(errorResponse, status);
    }

    // Validation / Business Errors
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(
        IllegalArgumentException ex) {

        log.error("Validation error: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .message(ex.getMessage())
            .status(HttpStatus.BAD_REQUEST.value())
            .timestamp(LocalDateTime.now())
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Generic Fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
        Exception ex) {

        log.error("Unexpected error", ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
            .message("Internal Server Error")
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .timestamp(LocalDateTime.now())
            .build();

        return new ResponseEntity<>(errorResponse,
            HttpStatus.INTERNAL_SERVER_ERROR);
    }
}