package com.agriconnect.agriconnect.controller.abonnement;

import com.agriconnect.agriconnect.paiement.PaiementService;
import com.agriconnect.agriconnect.paiement.PasserellePaiement;
import com.agriconnect.agriconnect.paiement.SimulationStore;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

// ===== SimulationPaiementController.java =====  (DÉVELOPPEMENT uniquement : n'existe pas si simulation-active=false)
@RestController
@RequestMapping("/api/dev/paiements")
@ConditionalOnProperty(name = "agriconnect.paiement.simulation-active", havingValue = "true")
@RequiredArgsConstructor
public class SimulationPaiementController {

    private final SimulationStore store;
    private final PaiementService paiementService;

    /** POST /api/dev/paiements/PAY-XXXX/reussi   ou   /echec */
    @PostMapping("/{reference}/{resultat}")
    public void simuler(@PathVariable String reference, @PathVariable String resultat) {
        store.definir(reference, "reussi".equals(resultat)
                ? PasserellePaiement.StatutExterne.REUSSI : PasserellePaiement.StatutExterne.ECHOUE);
        paiementService.actualiserParReference(reference);
    }
}