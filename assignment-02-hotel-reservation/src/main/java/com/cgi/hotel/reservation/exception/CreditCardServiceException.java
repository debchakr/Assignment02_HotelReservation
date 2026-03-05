package com.cgi.hotel.reservation.exception;

public class CreditCardServiceException extends RuntimeException {

    private final int statusCode;

    public CreditCardServiceException(String message) {
        super(message);
        this.statusCode = 500;
    }

    public CreditCardServiceException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public CreditCardServiceException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 500;
    }

    public int getStatusCode() {
        return statusCode;
    }
}