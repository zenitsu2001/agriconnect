package com.agriconnect.agriconnect.services.dto.marche.response;

import com.agriconnect.agriconnect.services.dto.metier.response.UtilisateurResume;

import java.time.LocalDateTime;

public record AvisResponse(Long id, int note, String commentaire, UtilisateurResume auteur, LocalDateTime date) {}
