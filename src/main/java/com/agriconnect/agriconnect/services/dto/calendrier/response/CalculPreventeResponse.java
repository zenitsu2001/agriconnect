package com.agriconnect.agriconnect.services.dto.calendrier.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CalculPreventeResponse(Long id, String produitCode, BigDecimal quantite, String unite,
                                     BigDecimal prixUnitaire, BigDecimal coutProduction, BigDecimal fraisTransport,
                                     BigDecimal autresFrais, BigDecimal tauxRemise, BigDecimal tauxTaxe,
                                     BigDecimal montantBrut, BigDecimal montantRemise, BigDecimal montantTaxe,
                                     BigDecimal coutTotal, BigDecimal montantFinal, BigDecimal beneficeNet,
                                     BigDecimal margeBeneficiaire, Boolean prixDansBareme,
                                     LocalDateTime dateCalcul) {}
