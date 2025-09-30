package com.kjmate.kjmate_back.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Getter
    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    // secretKey 생성
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // access token 생성
    public String generateAccessToken(String email, Long memberId, Character nationality) {
        return Jwts.builder()
                .subject(email)
                .claim("memberId",memberId)
                .claim("nationality",nationality.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    //refresh token 생성
    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰에서 이메일 추출
    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    // 토큰에서 멤버id 추출
    public Long getMemberIdFromToken(String token) {
        return getClaims(token).get("memberId", Long.class);
    }

    // 토큰에서 국적 추출
    public Character getNationalityFromToken(String token) {
        String nationality = getClaims(token).get("nationality", String.class);
        return nationality != null ? nationality.charAt(0) : null;
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try{
            getClaims(token);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    // claims 추출
    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
