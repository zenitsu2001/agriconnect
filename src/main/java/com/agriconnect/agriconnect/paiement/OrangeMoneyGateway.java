package com.agriconnect.agriconnect.paiement;

// ===== OrangeMoneyGateway.java =====  (SQUELETTE : à compléter avec la documentation de VOTRE compte Orange)
import com.agriconnect.agriconnect.entity.abonnement.Paiement;
import com.agriconnect.agriconnect.enums.ModePaiement;
import com.agriconnect.agriconnect.exception.paiement.PasserelleException;

import java.util.Map;
import java.util.Optional;

/**
 * Orange Money « Web Payment » : paiement par redirection, comme Wave.
 * Les sources que j'ai consultées décrivent ce déroulement, mais se contredisent sur les adresses de
 * l'API et sur la fiabilité des notifications. Rien n'est donc codé « au hasard » ici.
 *
 * Déroulement à implémenter (valider chaque appel dans l'espace développeur Orange) :
 *   1. Obtenir un jeton d'accès OAuth (identifiants du compte marchand) ; il expire, le renouveler.
 *   2. creerSession : appeler l'API « webpayment » avec notre référence (order_id), le montant,
 *      return_url / cancel_url / notif_url ; Orange renvoie pay_token + payment_url.
 *      -> SessionPaiement(referenceExterne = pay_token, urlPaiement = payment_url)
 *   3. verifier : appeler l'API de statut avec (order_id, montant, pay_token) -> SUCCESS / FAILED / ...
 *      Contrôler que montant et référence correspondent, comme dans WaveGateway.
 *   4. lireNotification : selon la documentation, vérifier le jeton de notification (notif_token)
 *      ou ignorer la notification et se fier à verifier() : le rattrapage planifié couvre déjà ce cas.
 */
public class OrangeMoneyGateway implements PasserellePaiement {

    private static final String MESSAGE = "Intégration Orange Money non implémentée : à compléter avec la documentation de votre compte marchand";

    @Override public ModePaiement mode() { return ModePaiement.ORANGE_MONEY; }
    @Override public String fournisseur() { return "orange"; }

    @Override public SessionPaiement creerSession(DemandeSession demande) { throw new PasserelleException(MESSAGE); }
    @Override public StatutExterne verifier(Paiement paiement) { throw new PasserelleException(MESSAGE); }
    @Override public Optional<NotificationPaiement> lireNotification(String corps, Map<String, String> entetes) {
        throw new PasserelleException(MESSAGE);
    }
}