package com.demo.ClassBuddy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {UserAlreadyExistsException.class})
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.CONFLICT,
                Timestamp.valueOf(LocalDateTime.now())
        );
        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.UNAUTHORIZED,
                Timestamp.valueOf(LocalDateTime.now())
        );
        return new ResponseEntity<>(apiException, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {ClassroomNotFound.class})
    public ResponseEntity<Object> handleClassroomNotFoundException(ClassroomNotFound e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.NOT_FOUND,
                Timestamp.valueOf(LocalDateTime.now())
        );
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {StudentAlreadyEnrolledException.class})
    public ResponseEntity<Object> handleAlreadyEnrolledException(StudentAlreadyEnrolledException e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.CONFLICT,
                Timestamp.valueOf(LocalDateTime.now())
        );
        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    @ResponseBody
    public ResponseEntity<Object> handleClassroomAlreadyExistsException(AuthenticationException e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.UNAUTHORIZED,
                Timestamp.valueOf(LocalDateTime.now())
        );
        return new ResponseEntity<>(apiException, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {ClassroomAlreadyExistsException.class})
    @ResponseBody
    public ResponseEntity<Object> handleClassroomAlreadyExistsException(ClassroomAlreadyExistsException e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.CONFLICT,
                Timestamp.valueOf(LocalDateTime.now())
        );
        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {UnauthorizedException.class})
    @ResponseBody
    public ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                HttpStatus.UNAUTHORIZED,
                Timestamp.valueOf(LocalDateTime.now())
        );
        return new ResponseEntity<>(apiException, HttpStatus.UNAUTHORIZED);
    }
}
