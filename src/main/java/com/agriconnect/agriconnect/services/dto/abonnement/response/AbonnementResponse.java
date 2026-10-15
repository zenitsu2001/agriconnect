package com.agriconnect.agriconnect.services.dto.abonnement.response;

import java.time.LocalDateTime;

public record AbonnementResponse(Long id, PlanResponse plan, String statut, LocalDateTime dateDebut,
                                 LocalDateTime dateFin, boolean renouvellementAuto, PaiementResponse paiement) {}
