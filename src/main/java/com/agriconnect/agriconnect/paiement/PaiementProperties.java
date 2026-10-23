package com.agriconnect.agriconnect.paiement;

// ===== PaiementProperties.java =====
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "agriconnect.paiement")
public record PaiementProperties(String frontendUrl, String urlPubliqueServeur,
                                 boolean simulationActive, Fournisseur wave, Fournisseur orange) {

    public record Fournisseur(boolean actif, String mode, String baseUrl, String cle, String secretWebhook) {}
}