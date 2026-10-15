package com.agriconnect.agriconnect.services.dto.calendrier.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BaremePrixResponse(Long id, String produitCode, String unite, BigDecimal prixReference,
                                 BigDecimal prixMin, BigDecimal prixMax, LocalDateTime dateModification) {}
