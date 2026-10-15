package com.agriconnect.agriconnect.services.dto.marche.response;

import com.agriconnect.agriconnect.services.dto.metier.response.UtilisateurResume;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CommandeResponse(Long id, ProduitResume produit, UtilisateurResume acheteur, BigDecimal quantite,
                               BigDecimal prixUnitaire, BigDecimal montantTotal, String statut,
                               String adresseLivraison, LocalDate dateLivraisonSouhaitee,
                               LocalDateTime dateCreation) {}