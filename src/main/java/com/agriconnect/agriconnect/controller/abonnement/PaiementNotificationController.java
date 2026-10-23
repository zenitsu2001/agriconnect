package com.agriconnect.agriconnect.controller.abonnement;

import com.agriconnect.agriconnect.enums.FrontCodes;
import com.agriconnect.agriconnect.enums.ModePaiement;
import com.agriconnect.agriconnect.paiement.PaiementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

// ===== PaiementNotificationController.java =====  (PUBLIC : protégé par la signature, pas par le jeton)
@RestController
@RequestMapping("/api/paiements/notifications")
@RequiredArgsConstructor
public class PaiementNotificationController {

    private final PaiementService paiementService;

    /** Le prestataire (Wave, Orange) appelle cette adresse. Répondre 200 vite : sinon il réessaie. */
    @PostMapping("/{mode}")
    public ResponseEntity<Void> notification(@PathVariable String mode, @RequestBody String corps,
                                             @RequestHeader HttpHeaders entetes) {
        // Le CORPS BRUT est nécessaire pour vérifier la signature : on le reçoit en texte, sans le transformer
        Map<String, String> enMinuscules = new HashMap<>();
        entetes.forEach((nom, valeurs) -> enMinuscules.put(nom.toLowerCase(), valeurs.get(0)));

        paiementService.traiterNotification(FrontCodes.fromCode(ModePaiement.class, mode), corps, enMinuscules);
        return ResponseEntity.ok().build();
    }
}