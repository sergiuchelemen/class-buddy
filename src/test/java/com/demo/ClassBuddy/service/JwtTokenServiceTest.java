package com.demo.ClassBuddy.service;

import com.demo.ClassBuddy.model.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.WeakKeyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class JwtTokenServiceTest {
    User user;
    String token;
    String refreshToken;

    private final JwtTokenService jwtTokenService = JwtTokenService.builder()
            .ACCESS_TOKEN_EXP_TIME(3600)
            .REFRESH_TOKEN_EXP_TIME(86400)
            .SECRET_KEY("765dccfb6644ca95e30e8a1c6965f81525eb8c30e7db8fc02de927872c861e63")
            .build();

    @BeforeEach
    public void setup() {
        user = User.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@gmail.com")
                .password("1234")
                .build();

        token = jwtTokenService.generateAccessToken(user);
        refreshToken = jwtTokenService.generateRefreshToken(user);
    }

    @Test
    public void testGenerateAccessToken() {
        String email = jwtTokenService.extractEmail(token);

        assertEquals("john.doe@gmail.com", email);
    }

    @Test
    public void testGenerateRefreshToken() {
        String email = jwtTokenService.extractEmail(refreshToken);

        assertEquals("john.doe@gmail.com", email);
    }

    @Test
    public void testGenerateTokenWithInvalidKey() {
        var jwtTokenService = JwtTokenService.builder()
                .ACCESS_TOKEN_EXP_TIME(3600)
                .REFRESH_TOKEN_EXP_TIME(86400)
                .SECRET_KEY("1234")
                .build();

        assertThrows(WeakKeyException.class, () -> jwtTokenService.generateRefreshToken(user));
    }

    @Test
    public void testExtractEmail() {
        String email = jwtTokenService.extractEmail(token);

        assertEquals("john.doe@gmail.com", email);
    }

    @Test
    public void testIsTokenValidWithValidEmail() {
        boolean isValid = jwtTokenService.isTokenValid(token, user);

        assertTrue(isValid);
    }

    @Test
    public void testIsTokenValidWithInvalidEmail() {
        user.setEmail("fake.email@gmail.com");

        boolean isValid = jwtTokenService.isTokenValid(token, user);

        assertFalse(isValid);
    }

    @Test
    public void testIsTokenValidWithNoEmail() {
        user.setEmail("");

        String invalidToken = jwtTokenService.generateAccessToken(user);

        assertThrows(NullPointerException.class, () -> jwtTokenService.isTokenValid(invalidToken, user));
    }

    @Test
    public void testIsTokenValidWithValidExpTime() {
        boolean isValid = jwtTokenService.isTokenValid(token, user);

        assertTrue(isValid);
    }

    @Test
    public void testIsTokenValidWithInvalidExpTime() {
        var jwtTokenService = JwtTokenService.builder()
                .ACCESS_TOKEN_EXP_TIME(0)
                .REFRESH_TOKEN_EXP_TIME(86400)
                .SECRET_KEY("765dccfb6644ca95e30e8a1c6965f81525eb8c30e7db8fc02de927872c861e63")
                .build();

        String invalidToken = jwtTokenService.generateAccessToken(user);

        assertThrows(ExpiredJwtException.class, () -> jwtTokenService.isTokenValid(invalidToken, user));
    }
}