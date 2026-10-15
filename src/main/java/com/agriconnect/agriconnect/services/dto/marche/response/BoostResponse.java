package com.agriconnect.agriconnect.services.dto.marche.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BoostResponse(Long id, LocalDateTime dateDebut, LocalDateTime dateFin, BigDecimal montant) {}
