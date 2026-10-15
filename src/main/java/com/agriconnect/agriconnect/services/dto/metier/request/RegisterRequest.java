package com.agriconnect.agriconnect.services.dto.metier.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// ===== Authentification et profil =====
// Noms de champs identiques au formulaire d'inscription Angular.
public record RegisterRequest(
        @NotBlank @Size(max = 80) String nom,
        @NotBlank @Size(max = 80) String prenom,
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(max = 30) String telephone,
        @NotBlank String profil,                        // code : agriculteur, client, ...
        @NotBlank @Size(max = 255) String adresse,
        @NotBlank @Size(min = 8, max = 100) String motDePasse,
        @NotBlank String confirmerMotDePasse,
        String langue,                                  // code : fr, en, wo (sinon langue par défaut du profil)
        // facultatifs, selon le profil
        String nomExploitation, String nomEntreprise, String typeClient,
        String organisation, String institution) {}

