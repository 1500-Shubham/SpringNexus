package com.example.AuthenticationService.ErrorHandling;

public class JWTCustomException extends RuntimeException {
    public JWTCustomException(String message) {
        super(message);
    }

    public JWTCustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
