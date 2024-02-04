package com.demo.ClassBuddy.utility;

import java.time.ZonedDateTime;

public record RegisterResponse(String message, ZonedDateTime timestamp, String username) {}
