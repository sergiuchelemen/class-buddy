package com.demo.ClassBuddy.utility;

import java.sql.Timestamp;

public record RefreshTokenResponse(String accessToken, Timestamp timestamp) { }
