package com.agriconnect.agriconnect.services.dto.marche;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "agriconnect.marche")
public record MarcheProperties(boolean avisReservesAuxAcheteurs, int taillePageMax) {}
