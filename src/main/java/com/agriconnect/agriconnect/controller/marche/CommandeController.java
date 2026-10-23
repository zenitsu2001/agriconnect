package com.agriconnect.agriconnect.controller.marche;

import com.agriconnect.agriconnect.services.dto.marche.request.CommandeRequest;
import com.agriconnect.agriconnect.services.dto.marche.request.CommandeStatutRequest;
import com.agriconnect.agriconnect.services.dto.marche.response.CommandeResponse;
import com.agriconnect.agriconnect.services.dto.parametrage.response.PageResponse;
import com.agriconnect.agriconnect.services.impl.marche.CommandeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
public class CommandeController {

    private final CommandeService commandeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommandeResponse creer(@Valid @RequestBody CommandeRequest request) {
        return commandeService.creer(request);
    }

    @GetMapping("/mes-achats")
    public PageResponse<CommandeResponse> mesAchats(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "12") int taille) {
        return commandeService.mesAchats(page, taille);
    }

    /** PATCH /api/commandes/5/statut   {"statut":"confirmed"} */
    @PatchMapping("/{id}/statut")
    public CommandeResponse changerStatut(@PathVariable Long id, @Valid @RequestBody CommandeStatutRequest request) {
        return commandeService.changerStatut(id, request);
    }
}