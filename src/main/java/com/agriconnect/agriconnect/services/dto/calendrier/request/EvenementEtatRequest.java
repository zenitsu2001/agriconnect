package com.agriconnect.agriconnect.services.dto.calendrier.request;

// EvenementEtatRequest.java (package dto) : corps de PATCH .../{id}/etat
import jakarta.validation.constraints.NotNull;

public record EvenementEtatRequest(@NotNull Boolean termine) {}