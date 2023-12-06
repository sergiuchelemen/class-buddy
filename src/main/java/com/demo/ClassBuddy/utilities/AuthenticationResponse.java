package com.demo.ClassBuddy.utilities;


import java.time.ZonedDateTime;

public record AuthenticationResponse(String message, ZonedDateTime timestamp, String accessToken, String refreshToken) {}
