package com.agriconnect.agriconnect.services.dto.messagerie.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PublicationRequest(@NotBlank @Size(max = 200) String titre, @NotBlank String contenu,
                                 @NotBlank String categorie, boolean publier, boolean notifierAgriculteurs,
                                 List<String> regionsCodes) {}   // vide = toutes les régions