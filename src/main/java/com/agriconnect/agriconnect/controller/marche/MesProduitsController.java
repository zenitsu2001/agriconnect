package com.agriconnect.agriconnect.controller.marche;

import com.agriconnect.agriconnect.services.dto.marche.response.ProduitResponse;
import com.agriconnect.agriconnect.services.dto.parametrage.response.PageResponse;
import com.agriconnect.agriconnect.services.impl.marche.ProduitService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/agriculteurs/me/produits")
@PreAuthorize("hasRole('AGRICULTEUR')")
@RequiredArgsConstructor
public class MesProduitsController {

    private final ProduitService produitService;

    @GetMapping
    public PageResponse<ProduitResponse> mesProduits(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "12") int taille) {
        return produitService.mesProduits(page, taille);
    }
}