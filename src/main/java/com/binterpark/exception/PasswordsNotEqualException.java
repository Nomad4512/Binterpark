package com.binterpark.exception;

public class PasswordsNotEqualException extends RuntimeException{
    public PasswordsNotEqualException(String message) {
        super(message);
    }
}
