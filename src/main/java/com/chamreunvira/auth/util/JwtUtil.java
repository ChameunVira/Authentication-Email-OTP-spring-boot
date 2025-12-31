package com.chamreunvira.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secrete;
    @Value("${jwt.expiration}")
    private Long expiration;

    private Key getSignKey() {
        byte[] byteKey = Decoders.BASE64.decode(secrete);
        return Keys.hmacShaKeyFor(byteKey);
    }
    public String generateToken(UserDetails userDetails) {
        return builderToken(new HashMap<>() , userDetails.getUsername() , expiration);
    }

    private String builderToken(Map<String , ?> claims , String subject , Long expiration) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey() , SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUserEmail(String token) {
        return extractClaims(token , Claims::getSubject);
    }

    public Date getExpiration(String token) {
        return extractClaims(token , Claims::getExpiration);
    }

    public boolean isTokenExpiry(String token) {
        return getExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token , UserDetails userDetails) {
        final String email = extractUserEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpiry(token));
    }

    private <T>T extractClaims(String token, Function<Claims , T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
