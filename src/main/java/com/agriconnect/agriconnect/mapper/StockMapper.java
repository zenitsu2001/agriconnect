package com.agriconnect.agriconnect.mapper;

import com.agriconnect.agriconnect.dto.StockRequest;
import com.agriconnect.agriconnect.dto.StockResponse;
import com.agriconnect.agriconnect.entity.Stock;
import org.mapstruct.*;

/**
 * Mapper pour l'entité Stock.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StockMapper {

    /**
     * Request → Entité.
     * agriculteurId (Long) est mappé vers l'objet imbriqué agriculteur.id.
     */
    @Mapping(source = "agriculteurId", target = "agriculteur.id")
    Stock toEntity(StockRequest request);

    /**
     * Entité → Response.
     * On extrait l'ID et le nom complet de l'agriculteur depuis l'objet imbriqué.
     */
    @Mapping(source = "agriculteur.id", target = "agriculteurId")
    @Mapping(
            expression = "java(stock.getAgriculteur().getNom() + \" \" + stock.getAgriculteur().getPrenom())",
            target = "agriculteurNomComplet"
    )
    StockResponse toResponse(Stock stock);
}