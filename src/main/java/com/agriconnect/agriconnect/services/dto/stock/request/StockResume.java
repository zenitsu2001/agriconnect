package com.agriconnect.agriconnect.services.dto.stock.request;

import java.math.BigDecimal;

public record StockResume(BigDecimal totalKg, long nombreStockBas, long entreesAujourdhui) {}

