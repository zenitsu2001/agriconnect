package com.agriconnect.agriconnect.controller.metier;

import com.agriconnect.agriconnect.services.dto.metier.request.UpdateProfilRequest;
import com.agriconnect.agriconnect.services.dto.metier.response.UtilisateurResponse;
import com.agriconnect.agriconnect.services.impl.metier.UtilisateurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @GetMapping
    public UtilisateurResponse moi() {
        return utilisateurService.moi();
    }

    @PatchMapping
    public UtilisateurResponse mettreAJour(@Valid @RequestBody UpdateProfilRequest request) {
        return utilisateurService.mettreAJourProfil(request);
    }
}