package com.goodme.framework.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT Token 提供者
 */
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret:goodmeSecretKey}")
    private String secret;

    @Value("${jwt.expiration:86400}")
    private long expiration;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        // 使用HMAC SHA算法，生成更安全的密钥
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 根据用户ID生成JWT
     *
     * @param userId 用户ID
     * @return JWT token
     */
    public String generateToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return generateToken(claims);
    }

    /**
     * 从JWT token获取用户ID
     *
     * @param token JWT token
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("userId", Long.class);
    }

    /**
     * 验证token是否有效
     *
     * @param token JWT token
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取token过期时间
     *
     * @return 过期时间（秒）
     */
    public long getExpirationInSeconds() {
        return expiration;
    }

    /**
     * 生成token
     *
     * @param claims 数据声明
     * @return token
     */
    private String generateToken(Map<String, Object> claims) {
        Date createdDate = new Date();
        Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从token中获取JWT中的负载
     *
     * @param token token
     * @return Claims
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
} 