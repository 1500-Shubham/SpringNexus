package com.example.AuthenticationService.service;

import com.example.AuthenticationService.ErrorHandling.JWTCustomException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret.key}")
    private String secretKey;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String generateToken(UserDetails user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isValid(String token, UserDetails user) {

            return extractUsername(token).equals(user.getUsername()) && !isTokenExpired(token);




    }
    // Add the validateToken method
    public boolean validateToken(String token) {
        try {
            // Check if the token is expired
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false; // If there is an exception (like invalid token), return false
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        // claims are the key-value pairs stored in the payload of a JWT
        //claims can contain information like the username, roles, expiration time, etc
        Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey()).build()
                .parseSignedClaims(token).getPayload();
        return claimsResolver.apply(claims);
    }
}
