package com.agriconnect.agriconnect.controller.metier;

import com.agriconnect.agriconnect.services.dto.metier.request.UpdateAgriculteurRequest;
import com.agriconnect.agriconnect.services.dto.metier.response.ExploitationResponse;
import com.agriconnect.agriconnect.services.impl.metier.AgriculteurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agriculteurs/me/exploitation")
@PreAuthorize("hasRole('AGRICULTEUR')")
@RequiredArgsConstructor
public class AgriculteurController {

    private final AgriculteurService agriculteurService;

    @GetMapping
    public ExploitationResponse exploitation() {
        return agriculteurService.exploitation();
    }

    @PutMapping
    public ExploitationResponse mettreAJour(@Valid @RequestBody UpdateAgriculteurRequest request) {
        return agriculteurService.mettreAJour(request);
    }
}