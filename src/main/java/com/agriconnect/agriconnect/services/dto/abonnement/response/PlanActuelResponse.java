package com.agriconnect.agriconnect.services.dto.abonnement.response;


import java.time.LocalDateTime;
import java.util.Set;

/** Plan réellement en vigueur pour l'utilisateur (le gratuit par défaut). `droits` = fonctionnalités premium débloquées. */
public record PlanActuelResponse(String planCode, boolean premium, LocalDateTime dateFin, Set<String> droits) {}