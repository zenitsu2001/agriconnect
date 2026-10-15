package com.agriconnect.agriconnect.services.dto.calendrier.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CalculPreventeRequest(String produitCode,
                                    @NotNull @DecimalMin("0.01") BigDecimal quantite, @NotBlank String unite,
                                    @NotNull @DecimalMin("0.01") BigDecimal prixUnitaire,
                                    @DecimalMin("0") BigDecimal coutProduction, @DecimalMin("0") BigDecimal fraisTransport,
                                    @DecimalMin("0") BigDecimal autresFrais,
                                    @DecimalMin("0") @DecimalMax("100") BigDecimal tauxRemise,
                                    @DecimalMin("0") @DecimalMax("100") BigDecimal tauxTaxe) {}
