package com.agriconnect.agriconnect.controller.abonnement;

import com.agriconnect.agriconnect.services.dto.abonnement.request.SouscriptionRequest;
import com.agriconnect.agriconnect.services.dto.abonnement.response.PlanActuelResponse;
import com.agriconnect.agriconnect.services.dto.abonnement.response.SouscriptionResponse;
import com.agriconnect.agriconnect.services.impl.AbonnementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

// ===== AbonnementController.java =====
@RestController
@RequestMapping("/api/abonnements")
@RequiredArgsConstructor
public class AbonnementController {

    private final AbonnementService abonnementService;

    @PostMapping("/souscrire")
    public SouscriptionResponse souscrire(@Valid @RequestBody SouscriptionRequest request) {
        return abonnementService.souscrire(request);
    }

    /** Plan en vigueur et droits débloqués : Angular s'en sert pour afficher ou masquer les fonctions premium. */
    @GetMapping("/mon-abonnement")
    public PlanActuelResponse monAbonnement() {
        return abonnementService.monPlan();
    }
}