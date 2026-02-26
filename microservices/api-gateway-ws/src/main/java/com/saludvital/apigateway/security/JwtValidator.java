package com.saludvital.apigateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtValidator {
    private final SecretKey key;

    public JwtValidator(@Value("${security.jwt.shared-secret}") String sharedSecret) {
        this.key = Keys.hmacShaKeyFor(sharedSecret.getBytes(StandardCharsets.UTF_8));
    }

    public Claims validate(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }
}
