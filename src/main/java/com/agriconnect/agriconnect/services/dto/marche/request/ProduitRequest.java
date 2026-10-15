package com.agriconnect.agriconnect.services.dto.marche.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record ProduitRequest(
        @NotBlank @Size(max = 150) String nom,
        @Size(max = 1000) String description,
        @NotBlank String categorie,
        @NotNull @DecimalMin("0.01") BigDecimal quantite,
        @NotBlank String unite,
        @NotNull @DecimalMin("0.01") BigDecimal prix,
        @Size(max = 3) List<@Size(max = 255) String> images,   // 3 photos maximum ; l'emoji sert de repli
        @Size(max = 16) String emoji,
        String produitReferenceCode,
        boolean booster) {}