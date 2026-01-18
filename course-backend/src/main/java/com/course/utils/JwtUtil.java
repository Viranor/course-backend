package com.course.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // 生成JWT令牌
    public String generateToken(String username, Long userId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);
        claims.put("username", username);

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expiration);

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(key)
                .compact();
    }

    // 从令牌中获取用户名
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // 从令牌中获取用户ID
    public Long getUserIdFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("userId", Long.class));
    }

    // 获取角色
    public String getRoleFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("role", String.class));
    }

    // 验证令牌是否有效
    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // 检查令牌是否过期
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // 获取过期时间
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // 通用的获取声明方法
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // 解析令牌
    private Claims getClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}