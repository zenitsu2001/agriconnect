package com.agriconnect.agriconnect.paiement;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "agriconnect.boost")
public record BoostProperties(BigDecimal prix, int dureeJours, boolean premiumRequis) {}
