package com.agriconnect.agriconnect.services.dto.metier.response;

import com.agriconnect.agriconnect.services.dto.parametrage.LocalisationDto;

import java.time.LocalDateTime;

public record UtilisateurResponse(
        Long id, String nom, String prenom, String email, String telephone, String adresse,
        String photoProfilUrl, String profil, String langue,
        LocalDateTime derniereConnexion, LocalDateTime dateCreation,
        // selon le profil (null sinon)
        String nomExploitation, LocalisationDto localisation,
        String nomEntreprise, String typeClient, String organisation, String institution) {}
