package com.agriconnect.agriconnect.services.dto.stock.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ArticleStockResponse(Long id, String nom, String type, BigDecimal quantite, String unite,
                                   BigDecimal seuilBas, BigDecimal seuilHaut, String statut,
                                   String regionCode, LocalDateTime dateModification) {}
