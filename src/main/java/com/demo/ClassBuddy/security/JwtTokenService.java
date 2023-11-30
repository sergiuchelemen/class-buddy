package com.demo.ClassBuddy.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.sql.Date;
import java.time.Instant;

@Service
public class JwtTokenService {
    final String SECRET_KEY = "765dccfb6644ca95e30e8a1c6965f81525eb8c30e7db8fc02de927872c861e63";

    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(1000 * 60 * 24)))
                .signWith(getSecretKey())
                .compact();
    }

    public SecretKey getSecretKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractPayload(String token){
         final JwtParser parser = Jwts.parser()
                .verifyWith(getSecretKey())
                .build();
         return parser.parseSignedClaims(token).getPayload();
    }

    public String extractEmail(String token){
        return extractPayload(token).getSubject();
    }

    private boolean isTokenExpired(String token) {
        return extractPayload(token).getExpiration().before(Date.from(Instant.now()));
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        String userEmail = extractEmail(token);
        return userEmail.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

}
