package com.agriconnect.agriconnect.services.dto.marche;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "agriconnect.fichiers")
public record FichierProperties(String dossier, long tailleMaxOctets) {}
