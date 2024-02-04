package com.demo.ClassBuddy.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String detail) {
        super(detail);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
