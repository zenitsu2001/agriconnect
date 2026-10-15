package com.agriconnect.agriconnect.services.dto.metier.response;

public record AuthResponse(String accessToken, String tokenType, long expiresIn, UtilisateurResponse utilisateur) {}


