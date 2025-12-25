package com.sportmatch.profileservice.exception;

public class ProfileAlreadyExistsException extends RuntimeException {
    public ProfileAlreadyExistsException(String message) {
        super(message);
    }
}