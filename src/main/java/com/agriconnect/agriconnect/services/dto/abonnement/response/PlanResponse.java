package com.agriconnect.agriconnect.services.dto.abonnement.response;

import java.math.BigDecimal;
import java.util.List;

// ===== Abonnements et paiements =====
public record PlanResponse(Long id, String code, BigDecimal prix, BigDecimal prixOriginal, String periode,
                           boolean populaire, List<String> fonctionnalites, List<String> limitations) {}
