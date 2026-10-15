package com.agriconnect.agriconnect.services.dto.metier.request;

import jakarta.validation.constraints.Size;

/** Mise à jour partielle : les champs null sont ignorés. */
public record UpdateProfilRequest(
        @Size(max = 80) String nom, @Size(max = 80) String prenom,
        @Size(max = 30) String telephone, @Size(max = 255) String adresse,
        @Size(max = 255) String photoProfilUrl, String langue) {}

