package com.demo.ClassBuddy.exception;

import org.springframework.security.core.AuthenticationException;

public class ExpiredTokenException extends AuthenticationException {
    public ExpiredTokenException(String message) {
        super(message);
    }
}
