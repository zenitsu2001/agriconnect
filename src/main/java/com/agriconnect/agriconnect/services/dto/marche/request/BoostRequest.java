package com.agriconnect.agriconnect.services.dto.marche.request;

import jakarta.validation.constraints.NotBlank;

public record BoostRequest(@NotBlank String modePaiement) {}