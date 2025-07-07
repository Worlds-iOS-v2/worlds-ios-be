package com.example.cultive.WorldStudy.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final String secret;
    private final Key secretKey;

    private final long accessTokenValidity = 1000 * 60 * 60 * 24;        // 1일
    private final long refreshTokenValidity = 1000L * 60 * 60 * 24 * 30; // 30일

    public JwtTokenProvider() {
        Dotenv dotenv = Dotenv.configure()
            .directory("/Users/gimnayeon/Project/Final/WorldStudy") // 또는 상대경로: "../../"
            .ignoreIfMissing() // (선택) 없을 때 예외 안 나게
            .load();

        this.secret = dotenv.get("JWT_SECRET");
        // 짧은 키를 32바이트로 패딩
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        byte[] paddedKey = new byte[32];
        System.arraycopy(keyBytes, 0, paddedKey, 0, Math.min(keyBytes.length, 32));
        this.secretKey = Keys.hmacShaKeyFor(paddedKey);
    }

    public String generateAccessToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
        return Long.parseLong(claims.getSubject()); // subject에 userId를 넣었다면
    }
}