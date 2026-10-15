package com.agriconnect.agriconnect.services.dto.stock.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record MouvementStockRequest(@NotBlank String type, @NotNull @DecimalMin("0.01") BigDecimal quantite,
                                    @Size(max = 255) String motif, Long destinataireId, String regionCode) {}

