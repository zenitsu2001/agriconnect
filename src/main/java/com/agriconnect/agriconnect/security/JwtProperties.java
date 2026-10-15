package com.agriconnect.agriconnect.security;

// ===== JwtProperties.java =====
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.time.Duration;

@ConfigurationProperties(prefix = "agriconnect.jwt")
public record JwtProperties(String secret, Duration expiration, String issuer) {}
