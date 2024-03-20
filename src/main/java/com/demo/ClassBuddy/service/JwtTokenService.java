package com.demo.ClassBuddy.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.sql.Date;
import java.time.Instant;

@Service
@Builder
public class JwtTokenService {
    private final long ACCESS_TOKEN_EXP_TIME;

    private final long REFRESH_TOKEN_EXP_TIME;

    private final String SECRET_KEY;

    public JwtTokenService(
            @Value("${accessTokenExpirationTime}") long accessTokenExpTime,
            @Value("${refreshTokenExpirationTime}") long refreshTokenExpTime,
            @Value("${SECRET_KEY}") String secretKey
    ) {
        this.ACCESS_TOKEN_EXP_TIME = accessTokenExpTime;
        this.REFRESH_TOKEN_EXP_TIME = refreshTokenExpTime;
        this.SECRET_KEY = secretKey;
    }

    public String generateAccessToken(UserDetails userDetails) {
        return buildToken(userDetails, ACCESS_TOKEN_EXP_TIME);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(userDetails, REFRESH_TOKEN_EXP_TIME);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String userEmail = extractEmail(token);
        return userEmail.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String extractEmail(String token) {
        return extractPayload(token).getSubject();
    }

    private String buildToken(UserDetails userDetails, long expirationTime) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(expirationTime)))
                .signWith(getSecretKey(SECRET_KEY))
                .compact();
    }

    private SecretKey getSecretKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractPayload(String token) {
        final JwtParser parser = Jwts.parser()
                .verifyWith(getSecretKey(SECRET_KEY))
                .build();
        return parser.parseSignedClaims(token).getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractPayload(token).getExpiration().before(Date.from(Instant.now()));
    }
}
