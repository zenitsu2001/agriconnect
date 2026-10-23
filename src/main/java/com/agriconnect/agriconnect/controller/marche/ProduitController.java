package com.agriconnect.agriconnect.controller.marche;

import com.agriconnect.agriconnect.services.dto.marche.request.ProduitRequest;
import com.agriconnect.agriconnect.services.dto.marche.response.ProduitResponse;
import com.agriconnect.agriconnect.services.dto.parametrage.response.PageResponse;
import com.agriconnect.agriconnect.services.impl.marche.ProduitService;
import com.agriconnect.agriconnect.services.impl.marche.VuesProduits;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
public class ProduitController {

    private final ProduitService produitService;
    private final VuesProduits vues;      // à ajouter aux champs
    /** GET /api/produits?q=tomate&regionCode=thies&tri=priceLow&page=0&taille=12 */
    @GetMapping
    public PageResponse<ProduitResponse> rechercher(@RequestParam(required = false) String q,
                                                    @RequestParam(required = false) String regionCode,
                                                    @RequestParam(defaultValue = "recent") String tri,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "12") int taille) {
        return produitService.rechercher(q, regionCode, tri, page, taille);
    }

    @GetMapping("/{id}")
    public ProduitResponse detail(@PathVariable Long id) {
        vues.compter(id);
        return produitService.detail(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('AGRICULTEUR')")
    @ResponseStatus(HttpStatus.CREATED)
    public ProduitResponse creer(@Valid @RequestBody ProduitRequest request) {
        return produitService.creer(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('AGRICULTEUR')")
    public ProduitResponse modifier(@PathVariable Long id, @Valid @RequestBody ProduitRequest request) {
        return produitService.modifier(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('AGRICULTEUR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void retirer(@PathVariable Long id) {
        produitService.retirer(id);
    }


}