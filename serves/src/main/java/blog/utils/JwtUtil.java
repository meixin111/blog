package blog.utils;

import blog.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${jwt.secret:blog-secret-key-2024}")
    private String secret;

    @Value("${jwt.expiration:604800}")
    private Long expiration;

    private SecretKey getSigningKey() {
        // 确保密钥长度32位
        if (secret.length() < 32) {
            secret = secret + "0".repeat(32 - secret.length());
        } else if (secret.length() > 32) {
            secret = secret.substring(0, 32);
        }
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(User user) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", user.getId());
            claims.put("username", user.getUsername());

            return Jwts.builder()
                    .claims(claims)
                    .subject(user.getUsername())
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + expiration * 1000))
                    .signWith(getSigningKey(), Jwts.SIG.HS256)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("生成Token失败: " + e.getMessage());
        }
    }

    public Claims validateToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new RuntimeException("Token验证失败: " + e.getMessage());
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = validateToken(token);
        return claims.getSubject();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = validateToken(token);
        return claims.get("id", Long.class);
    }
}