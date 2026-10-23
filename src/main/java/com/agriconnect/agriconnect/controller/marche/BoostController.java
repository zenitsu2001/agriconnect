package com.agriconnect.agriconnect.controller.marche;

import com.agriconnect.agriconnect.services.dto.marche.request.BoostRequest;
import com.agriconnect.agriconnect.services.dto.marche.response.BoostCommandeResponse;
import com.agriconnect.agriconnect.services.impl.marche.BoostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

// ===== BoostController.java =====
@RestController
@RequestMapping("/api/produits")
@PreAuthorize("hasRole('AGRICULTEUR')")
@RequiredArgsConstructor
public class BoostController {

    private final BoostService boostService;

    /** POST /api/produits/12/boost   {"modePaiement":"wave"}   (corps inutile si le boost est gratuit) */
    @PostMapping("/{produitId}/boost")
    @ResponseStatus(HttpStatus.CREATED)
    public BoostCommandeResponse booster(@PathVariable Long produitId, @RequestBody(required = false) BoostRequest request) {
        return boostService.demarrer(produitId, request);
    }
}