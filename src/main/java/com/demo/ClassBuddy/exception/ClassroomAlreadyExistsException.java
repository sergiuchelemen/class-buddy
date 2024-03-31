package com.demo.ClassBuddy.exception;

public class ClassroomAlreadyExistsException extends RuntimeException {
    public ClassroomAlreadyExistsException(String message) {
        super(message);
    }
}
