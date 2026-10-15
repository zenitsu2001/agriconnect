package com.agriconnect.agriconnect.services.dto.calendrier.response;

import java.time.LocalDate;

public record EvenementResponse(Long id, String type, String titre, String description,
                                LocalDate dateEvenement, String priorite, boolean termine) {}

