package com.demo.ClassBuddy.utilities;

import java.time.ZonedDateTime;

public record RegisterResponse(String message, ZonedDateTime timestamp, String username) {}
