package com.demo.ClassBuddy.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String detail) {
        super(detail);
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
