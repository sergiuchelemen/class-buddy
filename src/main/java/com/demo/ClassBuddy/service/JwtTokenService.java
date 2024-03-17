package com.demo.ClassBuddy.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.sql.Date;
import java.time.Instant;

@Service
public class JwtTokenService {
    @Value("${accessTokenExpirationTime}")
    private long accessTokenExpirationTime;

    @Value("${refreshTokenExpirationTime}")
    private long refreshTokenExpirationTime;

    @Value("${SECRET_KEY}")
    private String secretKey;


    private String buildToken(UserDetails userDetails, long expirationTime) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(expirationTime)))
                .signWith(getSecretKey())
                .compact();
    }

    public String generateAccessToken(UserDetails userDetails) {
        return buildToken(userDetails, accessTokenExpirationTime);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(userDetails, refreshTokenExpirationTime);
    }


    public SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractPayload(String token) {
        final JwtParser parser = Jwts.parser()
                .verifyWith(getSecretKey())
                .build();
        return parser.parseSignedClaims(token).getPayload();
    }

    public String extractEmail(String token) {
        return extractPayload(token).getSubject();
    }

    private boolean isTokenExpired(String token) {
        return extractPayload(token).getExpiration().before(Date.from(Instant.now()));
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String userEmail = extractEmail(token);
        return userEmail.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}
