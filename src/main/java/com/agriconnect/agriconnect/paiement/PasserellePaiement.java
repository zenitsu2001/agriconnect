package com.agriconnect.agriconnect.paiement;

import com.agriconnect.agriconnect.entity.abonnement.Paiement;
import com.agriconnect.agriconnect.enums.ModePaiement;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

public interface PasserellePaiement {

    ModePaiement mode();

    /** Nom technique du prestataire, enregistré dans le paiement (wave, orange, simulation). */
    String fournisseur();

    /** Crée la session de paiement chez le prestataire et renvoie l'adresse où envoyer le client. */
    SessionPaiement creerSession(DemandeSession demande);

    /**
     * Interroge le prestataire pour connaître le statut RÉEL du paiement.
     * C'est la seule source de vérité : c'est elle qui décide d'activer un abonnement.
     */
    StatutExterne verifier(Paiement paiement);

    /**
     * Lit une notification (webhook) : vérifie sa SIGNATURE (lève SignatureInvalideException si elle est fausse)
     * puis renvoie notre référence de paiement. La notification ne sert que de déclencheur.
     * Les clés de `entetes` sont en minuscules.
     */
    Optional<NotificationPaiement> lireNotification(String corps, Map<String, String> entetes);

    record DemandeSession(String reference, BigDecimal montant, String devise, String description,
                          String urlSucces, String urlEchec, String urlNotification) {}

    record SessionPaiement(String referenceExterne, String urlPaiement) {}

    record NotificationPaiement(String reference) {}

    enum StatutExterne { EN_ATTENTE, REUSSI, ECHOUE }
}