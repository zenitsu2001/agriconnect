package com.agriconnect.agriconnect.paiement;

import com.agriconnect.agriconnect.services.impl.AbonnementService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// ===== AbonnementPlanification.java =====
@Component
@RequiredArgsConstructor
public class AbonnementPlanification {

    private final AbonnementService abonnementService;

    @Scheduled(cron = "0 5 * * * *")          // chaque heure, à la 5e minute
    public void expirer() {
        abonnementService.expirerTermines();
    }
}