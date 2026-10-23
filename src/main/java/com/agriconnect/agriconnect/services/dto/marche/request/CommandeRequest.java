package com.agriconnect.agriconnect.services.dto.marche.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CommandeRequest(@NotNull Long produitId, @NotNull @DecimalMin("0.01") BigDecimal quantite,
                              @Size(max = 255) String adresseLivraison,  @FutureOrPresent LocalDate dateLivraisonSouhaitee) {}
