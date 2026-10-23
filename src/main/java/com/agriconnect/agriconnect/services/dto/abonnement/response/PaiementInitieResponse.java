package com.agriconnect.agriconnect.services.dto.abonnement.response;

import java.math.BigDecimal;

/** Réponse à la création d'un paiement : l'adresse où envoyer le client pour payer. */
public record PaiementInitieResponse(String reference, String urlPaiement, String mode, String statut,
                                     BigDecimal montant, String devise) {}
