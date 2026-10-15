package com.agriconnect.agriconnect.services.dto.abonnement.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaiementResponse(Long id, BigDecimal montant, String devise, String mode, String statut,
                               String objet, String referenceExterne, LocalDateTime dateConfirmation) {}
