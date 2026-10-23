package com.agriconnect.agriconnect.controller.metier;

import com.agriconnect.agriconnect.services.dto.abonnement.response.PlanResponse;
import com.agriconnect.agriconnect.services.dto.marche.ProduitReferenceDto;
import com.agriconnect.agriconnect.services.dto.parametrage.CommuneDto;
import com.agriconnect.agriconnect.services.dto.parametrage.DepartementDto;
import com.agriconnect.agriconnect.services.dto.parametrage.LangueDto;
import com.agriconnect.agriconnect.services.dto.parametrage.RegionDto;
import com.agriconnect.agriconnect.services.impl.metier.ReferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/reference")
@RequiredArgsConstructor
public class ReferenceController {

    private final ReferenceService referenceService;

    @GetMapping("/langues")
    public ResponseEntity<List<LangueDto>> langues() {
        return cache(referenceService.langues());
    }

    @GetMapping("/regions")
    public ResponseEntity<List<RegionDto>> regions() {
        return cache(referenceService.regions());
    }

    @GetMapping("/regions/{code}/departements")
    public ResponseEntity<List<DepartementDto>> departements(@PathVariable String code) {
        return cache(referenceService.departements(code));
    }

    @GetMapping("/departements/{id}/communes")
    public ResponseEntity<List<CommuneDto>> communes(@PathVariable Long id) {
        return cache(referenceService.communes(id));
    }

    /** Autocomplétion : GET /api/reference/communes?regionCode=thies&q=mb */
    @GetMapping("/communes")
    public List<CommuneDto> rechercher(@RequestParam String regionCode, @RequestParam String q) {
        return referenceService.rechercherCommunes(regionCode, q);
    }

    @GetMapping("/produits")
    public ResponseEntity<List<ProduitReferenceDto>> produits() {
        return cache(referenceService.produits());
    }

    private static <T> ResponseEntity<T> cache(T corps) {
        return ResponseEntity.ok().cacheControl(CacheControl.maxAge(Duration.ofHours(1)).cachePublic()).body(corps);
    }

    @GetMapping("/plans")
    public ResponseEntity<List<PlanResponse>> plans() {
        return cache(referenceService.plans());
    }
}