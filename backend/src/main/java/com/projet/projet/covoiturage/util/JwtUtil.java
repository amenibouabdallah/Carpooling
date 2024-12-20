package com.projet.projet.covoiturage.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class JwtUtil {
    // Original secret key (you can customize this)
    private static final String CUSTOM_SECRET = "-6lMzOIsI9KYQmMhiV_rlPhJo4O_kiG3-yeVbJOKBFU";

    // Ensure the key is long enough by hashing it (using SHA-256 to get a 512-bit key)
    private static final Key SECRET_KEY = generateSecureKey(CUSTOM_SECRET);
    private static final long EXPIRATION_TIME = 86400000; // 1 day

    // Method to generate a secure key from the custom secret key (hashed)
    private static Key generateSecureKey(String secret) {
        try {
            // Using SHA-256 to hash the secret and ensure a 512-bit key
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] hashedKey = sha.digest(secret.getBytes(StandardCharsets.UTF_8));
            return Keys.hmacShaKeyFor(hashedKey); // Convert hashed key to valid HS512 key
        } catch (Exception e) {
            throw new RuntimeException("Error generating secret key", e);
        }
    }

    // Method to generate a JWT with email and role
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    // Method to extract the role from the token
    public String extractRole(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)  // Use the securely generated secret key for validation
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class);
// Extract the role
    }


    // Method to extract the email from the token
    public String extractEmail(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)  // Use the securely generated secret key for validation
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject(); // Extract the email
    }
}
