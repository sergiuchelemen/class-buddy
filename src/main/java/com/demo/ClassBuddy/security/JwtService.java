package com.demo.ClassBuddy.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.*;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtService {
    final String SECRET_KEY = "765dccfb6644ca95e30e8a1c6965f81525eb8c30e7db8fc02de927872c861e63";
    public String extractEmail(String token){
        return null;
    }

    public SecretKey getSecretKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private final JwtParser parser = Jwts.parser()
            .verifyWith(getSecretKey())
            .build();
    private final Claims claims = parser.parseSignedClaims("token").getPayload();

    // TODO: generate JWT token with claims
}
