package com.agriconnect.agriconnect.controller.abonnement;

import com.agriconnect.agriconnect.enums.ModePaiement;
import com.agriconnect.agriconnect.paiement.PaiementService;
import com.agriconnect.agriconnect.security.UtilisateurCourant;
import com.agriconnect.agriconnect.services.dto.abonnement.response.PaiementResponse;
import com.agriconnect.agriconnect.services.mapper.AbonnementMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// ===== PaiementController.java =====
@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
public class PaiementController {

    private final PaiementService paiementService;
    private final UtilisateurCourant courant;
    private final AbonnementMapper mapper;

    /** Modes proposés dans la fenêtre de paiement (codes : wave, orange...). */
    @GetMapping("/modes")
    public List<String> modes() {
        return paiementService.modesActifs().stream().map(ModePaiement::getCode).toList();
    }

    /** Statut d'un paiement (page de retour). L'appelant doit en être le propriétaire. */
    @GetMapping("/{reference}")
    public PaiementResponse statut(@PathVariable String reference) {
        return mapper.toDto(paiementService.statutPourUtilisateur(reference, courant.id()));
    }
}