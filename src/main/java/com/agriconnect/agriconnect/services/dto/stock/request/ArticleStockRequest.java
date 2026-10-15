package com.agriconnect.agriconnect.services.dto.stock.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ArticleStockRequest(@NotBlank @Size(max = 150) String nom, @NotBlank String type,
                                  @NotNull @DecimalMin("0") BigDecimal quantite, @NotBlank String unite,
                                  @DecimalMin("0") BigDecimal seuilBas, @DecimalMin("0") BigDecimal seuilHaut,
                                  String regionCode) {}

