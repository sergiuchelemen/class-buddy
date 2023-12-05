package com.demo.ClassBuddy.exceptions;

import javax.security.sasl.AuthenticationException;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String detail) {
        super(detail);
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
