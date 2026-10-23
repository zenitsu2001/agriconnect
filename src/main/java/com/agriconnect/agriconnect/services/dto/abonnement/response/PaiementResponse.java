package com.agriconnect.agriconnect.services.dto.abonnement.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// PaiementResponse.java : on n'expose plus les identifiants internes)
public record PaiementResponse(String reference, BigDecimal montant, String devise, String mode,
                               String statut, String objet, LocalDateTime dateConfirmation) {}
