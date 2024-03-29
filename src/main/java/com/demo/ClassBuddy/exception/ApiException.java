package com.demo.ClassBuddy.exception;

import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

public record ApiException(String message, HttpStatus httpStatus, Timestamp timestamp) {
}
