package com.myoffice.app.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Value("${jwt_secret}")
    private String secret;

    @Value("${jwt_expiration_minutes}")
    private long expiration;

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return
                extractUsername(token).equals(userDetails.getUsername())
                        &&
                        !extractExpiration(token).before(new Date());
    }
}