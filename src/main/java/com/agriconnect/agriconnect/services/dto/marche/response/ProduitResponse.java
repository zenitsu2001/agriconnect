package com.agriconnect.agriconnect.services.dto.marche.response;

import com.agriconnect.agriconnect.services.dto.metier.response.UtilisateurResume;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ProduitResponse(
        Long id, String nom, String description, String categorie, BigDecimal quantite, String unite,
        BigDecimal prix, List<String> images, String emoji, String statut, int nombreVues,
        Double noteMoyenne, long nombreAvis, boolean booste, String produitReferenceCode,
        UtilisateurResume agriculteur, String localisation, String regionCode, LocalDateTime dateCreation) {}
