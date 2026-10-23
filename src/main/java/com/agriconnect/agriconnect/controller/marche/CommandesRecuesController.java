package com.agriconnect.agriconnect.controller.marche;

import com.agriconnect.agriconnect.services.dto.marche.response.CommandeResponse;
import com.agriconnect.agriconnect.services.dto.parametrage.response.PageResponse;
import com.agriconnect.agriconnect.services.impl.marche.CommandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agriculteurs/me/commandes")
@PreAuthorize("hasRole('AGRICULTEUR')")
@RequiredArgsConstructor
public class CommandesRecuesController {

    private final CommandeService commandeService;

    @GetMapping
    public PageResponse<CommandeResponse> recues(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "12") int taille) {
        return commandeService.recues(page, taille);
    }
}