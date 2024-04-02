package com.demo.ClassBuddy.utility;

import com.demo.ClassBuddy.dto.UserDTO;

import java.time.ZonedDateTime;

public record RegisterResponse(String message, ZonedDateTime timestamp, UserDTO username) {
}
