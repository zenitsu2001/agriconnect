package com.agriconnect.agriconnect.controller.marche;

import com.agriconnect.agriconnect.services.dto.marche.request.AvisRequest;
import com.agriconnect.agriconnect.services.dto.marche.response.AvisResponse;
import com.agriconnect.agriconnect.services.impl.marche.AvisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ===== AvisController.java =====
@RestController
@RequestMapping("/api/produits/{produitId}/avis")
@RequiredArgsConstructor
public class AvisController {

    private final AvisService avisService;

    @GetMapping
    public List<AvisResponse> lister(@PathVariable Long produitId) {
        return avisService.lister(produitId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AvisResponse creer(@PathVariable Long produitId, @Valid @RequestBody AvisRequest request) {
        return avisService.creer(produitId, request);
    }
}