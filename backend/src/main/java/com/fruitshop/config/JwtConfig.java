package com.fruitshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${JWT_SECRET:fruitshop-jwt-secret-key-2026-very-long-secret}")
    private String secret;

    private long expiration = 7200000L; // 2 hours

    private long rememberExpiration = 604800000L; // 7 days

    public String getSecret() {
        return secret;
    }

    public long getExpiration() {
        return expiration;
    }

    public long getRememberExpiration() {
        return rememberExpiration;
    }
}
