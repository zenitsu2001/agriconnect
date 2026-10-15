package com.agriconnect.agriconnect.services.dto.parametrage.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LocalisationRequest(@NotBlank String regionCode, Long departementId, Long communeId,
                                  Double latitude, Double longitude, @Size(max = 150) String libelle) {}
