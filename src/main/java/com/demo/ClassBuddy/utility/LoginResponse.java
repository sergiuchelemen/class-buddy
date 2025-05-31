package com.demo.ClassBuddy.utility;

import com.demo.ClassBuddy.dto.UserDTO;

import java.time.ZonedDateTime;

public record LoginResponse(String message, ZonedDateTime timestamp, String accessToken, String refreshToken, UserDTO user) {
}
