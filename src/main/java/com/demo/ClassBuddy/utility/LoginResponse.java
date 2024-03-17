package com.demo.ClassBuddy.utility;

import java.time.ZonedDateTime;

public record LoginResponse(String message, ZonedDateTime timestamp, String accessToken, String refreshToken) {
}
