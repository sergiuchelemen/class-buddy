package com.demo.ClassBuddy.utility;

import com.demo.ClassBuddy.dto.UserDTO;

import java.sql.Timestamp;

public record RegisterResponse(String message, Timestamp timestamp, UserDTO newUser) {
}
