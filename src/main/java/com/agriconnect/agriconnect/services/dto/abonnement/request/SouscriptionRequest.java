package com.agriconnect.agriconnect.services.dto.abonnement.request;


import jakarta.validation.constraints.NotBlank;

public record SouscriptionRequest(@NotBlank String planCode, String modePaiement) {}
