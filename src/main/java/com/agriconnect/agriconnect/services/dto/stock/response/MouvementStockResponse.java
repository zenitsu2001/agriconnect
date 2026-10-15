package com.agriconnect.agriconnect.services.dto.stock.response;


import com.agriconnect.agriconnect.services.dto.metier.response.UtilisateurResume;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MouvementStockResponse(Long id, Long articleId, String type, BigDecimal quantite,
                                     LocalDateTime dateMouvement, String motif,
                                     UtilisateurResume destinataire, String regionCode) {}
