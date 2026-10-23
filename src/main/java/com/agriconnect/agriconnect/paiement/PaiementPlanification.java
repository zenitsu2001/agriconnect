package com.agriconnect.agriconnect.paiement;

// ===== PaiementPlanification.java =====  (rattrapage planifié)
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * Les notifications peuvent se perdre. Toutes les 5 minutes, on revérifie les paiements restés en attente.
 * (Classe séparée du service : une méthode qui s'appelle elle-même ne passerait pas par le proxy
 * transactionnel de Spring.)
 * ⚠️ Avec PLUSIEURS instances de l'application, ces tâches s'exécutent sur chacune : prévoir ShedLock.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaiementPlanification {

    private final PaiementService paiementService;

    @Scheduled(fixedDelay = 300_000, initialDelay = 60_000)
    public void rattraper() {
        LocalDateTime maintenant = LocalDateTime.now();
        // de 2 minutes (laisser le temps au webhook) à 24 heures
        for (String reference : paiementService.referencesEnAttente(maintenant.minusHours(24), maintenant.minusMinutes(2))) {
            try {
                paiementService.actualiserParReference(reference);
            } catch (RuntimeException e) {
                log.warn("Rattrapage impossible pour {} : {}", reference, e.getMessage());
            }
        }
        int abandonnes = paiementService.expirerAnciens(maintenant.minusHours(24));
        if (abandonnes > 0) log.info("{} paiement(s) abandonné(s) après 24 h", abandonnes);
    }
}