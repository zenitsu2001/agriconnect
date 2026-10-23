package com.agriconnect.agriconnect.paiement;

// ===== PaiementTraiteEvent.java =====
import com.agriconnect.agriconnect.enums.ObjetPaiement;
import com.agriconnect.agriconnect.enums.StatutPaiement;

/** Publié quand un paiement devient REUSSI ou ECHOUE : abonnements (et plus tard boosts) y réagissent. */
public record PaiementTraiteEvent(Long paiementId, ObjetPaiement objet, StatutPaiement statut) {}