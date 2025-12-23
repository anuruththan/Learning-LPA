package com.example.learning_jpa.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class AccessJwtUtil {
    @Value("${SECRET_KEY}")
    private String SECRET_KEY;

    @Value("${EXPIRATION_Time}")
    private int EXPIRATION_TIME;

    public String generateToken(String email, Integer role) {
        log.info(SECRET_KEY);
        String token;
        token = Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        log.info(token);
        return token;
    }

    public String extractEmail(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public Integer extractRole(String token) {
        return (Integer) Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().get("role");
    }

}
