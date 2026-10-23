package com.agriconnect.agriconnect.services.dto.abonnement.response;

/** abonnement et paiement sont null pour le plan gratuit ou si l'utilisateur a déjà un abonnement actif. */
public record SouscriptionResponse(AbonnementResponse abonnement, PaiementInitieResponse paiement) {}
