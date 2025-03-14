package com.example.AuthenticationService.ErrorHandling;


public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}