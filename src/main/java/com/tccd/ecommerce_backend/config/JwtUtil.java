package com.tccd.ecommerce_backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil
{
    @Value("${jwt.secret}")
    private String secret;
    private Key getSigningKey()
    {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String generateToken(Long userId)
    {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 24 * 1000 * 60 * 60);

        return Jwts.builder().subject(String.valueOf(userId)).issuedAt(now).expiration(expiry).signWith(getSigningKey()).compact();
    }
    public Long extractUserId(String token)
    {
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String subject = claims.getSubject();

        return Long.parseLong(subject);
    }
    public boolean isTokenValid(String token)
    {
        try
        {
            Jwts.parser()
                    .verifyWith((SecretKey) getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        }
        catch (JwtException e)
        {
            return false;
        }

    }
}
