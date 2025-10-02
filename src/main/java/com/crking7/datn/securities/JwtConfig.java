package com.crking7.datn.securities;

import com.crking7.datn.exceptions.LoginApiException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

@Component
public class JwtConfig {
    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationMillis;

    public String getJwtSecret() {
        return jwtSecret;
    }

    public long getJwtExpirationMillis() {
        return jwtExpirationMillis;
    }

    // generate token
    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationMillis);

        SecretKey secretKey = new SecretKeySpec(jwtSecret.getBytes(), SignatureAlgorithm.HS512.getJcaName());

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .claim("roles", roles)
                .signWith(secretKey)
                .compact();
    }

    // get username from the token
    public  String getUsernameFromJWT(String token){
        SecretKey secretKey = new SecretKeySpec(jwtSecret.getBytes(), SignatureAlgorithm.HS512.getJcaName());

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
    // Blacklist to store invalidated tokens
    private Set<String> blacklistedTokens = new HashSet<>();

    // Add token to the blacklist
    public void addToBlacklist(String token) {
        blacklistedTokens.add(token);
    }

    // Check if token is blacklisted
    public boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    // validate JWT token
    public  boolean validateToken(String token){
        try {
            SecretKey secretKey = new SecretKeySpec(jwtSecret.getBytes(), SignatureAlgorithm.HS512.getJcaName());
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);

            // Check if token is blacklisted
            if (isBlacklisted(token)) {
                throw new JwtException("Token is blacklisted");
            }

            // Validate expiration and other claims here...

            return true;
        } catch (JwtException ex) {
            throw LoginApiException.handleJwtException(ex);
        }
    }
}
