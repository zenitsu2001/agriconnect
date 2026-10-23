package com.agriconnect.agriconnect.services.impl.metier;

import com.agriconnect.agriconnect.exception.ApiException;
import com.agriconnect.agriconnect.services.impl.AbonnementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// ===== DroitsService.java =====  (à appeler dans les services réservés aux abonnés)
@Service
@RequiredArgsConstructor
public class DroitsService {

    private final AbonnementService abonnementService;

    /**
     * Refuse (403, errors.subscription.premiumRequired) si la fonctionnalité est premium et que
     * l'utilisateur n'a pas d'abonnement payant en cours.
     * Exemple : droitsService.exiger(courant.id(), "smartPresale");
     */
    public void exiger(Long utilisateurId, String fonctionnalite) {
        boolean reservee = AbonnementService.FONCTIONNALITES_PREMIUM.contains(fonctionnalite);
        if (reservee && !abonnementService.planActuel(utilisateurId).premium()) {
            throw ApiException.forbidden("errors.subscription.premiumRequired");
        }
    }
}