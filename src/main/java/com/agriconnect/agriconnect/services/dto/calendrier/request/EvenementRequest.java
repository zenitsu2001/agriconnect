package com.agriconnect.agriconnect.services.dto.calendrier.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record EvenementRequest(@NotBlank String type, @NotBlank @Size(max = 150) String titre,
                               @Size(max = 500) String description, @NotNull LocalDate dateEvenement,
                               String priorite) {}

