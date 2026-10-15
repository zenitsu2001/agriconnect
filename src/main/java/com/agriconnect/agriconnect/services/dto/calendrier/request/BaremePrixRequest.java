package com.agriconnect.agriconnect.services.dto.calendrier.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record BaremePrixRequest(@NotBlank String produitCode, @NotBlank String unite,
                                @NotNull @DecimalMin("0") BigDecimal prixReference,
                                @DecimalMin("0") BigDecimal prixMin, @DecimalMin("0") BigDecimal prixMax) {}
