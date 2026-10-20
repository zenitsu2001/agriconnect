package com.agriconnect.agriconnect.controller.calendrier;


import com.agriconnect.agriconnect.services.dto.calendrier.request.EvenementEtatRequest;
import com.agriconnect.agriconnect.services.dto.calendrier.request.EvenementRequest;
import com.agriconnect.agriconnect.services.dto.calendrier.response.EvenementResponse;
import com.agriconnect.agriconnect.services.impl.calendrier.EvenementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/agriculteurs/me/evenements")
@PreAuthorize("hasRole('AGRICULTEUR')")
@RequiredArgsConstructor
public class EvenementController {

    private final EvenementService evenementService;

    /** GET /api/agriculteurs/me/evenements?debut=2026-10-01&fin=2026-10-31 */
    @GetMapping
    public List<EvenementResponse> lister(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return evenementService.lister(debut, fin);
    }

    /** GET /api/agriculteurs/me/evenements/a-venir?limite=5 */
    @GetMapping("/a-venir")
    public List<EvenementResponse> aVenir(@RequestParam(defaultValue = "5") int limite) {
        return evenementService.aVenir(limite);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EvenementResponse creer(@Valid @RequestBody EvenementRequest request) {
        return evenementService.creer(request);
    }

    @PutMapping("/{id}")
    public EvenementResponse modifier(@PathVariable Long id, @Valid @RequestBody EvenementRequest request) {
        return evenementService.modifier(id, request);
    }

    /** PATCH /api/agriculteurs/me/evenements/12/etat   {"termine": true} */
    @PatchMapping("/{id}/etat")
    public EvenementResponse definirEtat(@PathVariable Long id, @Valid @RequestBody EvenementEtatRequest request) {
        return evenementService.definirEtat(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        evenementService.supprimer(id);
    }

    /** GET /api/agriculteurs/me/evenements/rappels?jours=7&limite=5 */
    @GetMapping("/rappels")
    public List<EvenementResponse> rappels(@RequestParam(defaultValue = "7") int jours,
                                           @RequestParam(defaultValue = "5") int limite) {
        return evenementService.rappels(jours, limite);
    }
}