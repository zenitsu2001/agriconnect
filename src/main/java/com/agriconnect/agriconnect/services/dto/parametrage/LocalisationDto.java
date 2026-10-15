package com.agriconnect.agriconnect.services.dto.parametrage;

public record LocalisationDto(String regionCode, Long departementId, String departementNom,
                              Long communeId, String communeNom,
                              Double latitude, Double longitude, String libelle) {}
