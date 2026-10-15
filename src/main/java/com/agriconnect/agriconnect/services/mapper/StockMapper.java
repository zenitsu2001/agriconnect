package com.agriconnect.agriconnect.services.mapper;

import com.agriconnect.agriconnect.entity.stock.ArticleStock;
import com.agriconnect.agriconnect.entity.stock.MouvementStock;
import com.agriconnect.agriconnect.services.config.CentralConfig;
import com.agriconnect.agriconnect.services.config.ReferenceResolver;
import com.agriconnect.agriconnect.services.dto.stock.request.ArticleStockRequest;
import com.agriconnect.agriconnect.services.dto.stock.request.MouvementStockRequest;
import com.agriconnect.agriconnect.services.dto.stock.response.ArticleStockResponse;
import com.agriconnect.agriconnect.services.dto.stock.response.MouvementStockResponse;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

// ===== StockMapper.java =====
@Mapper(config = CentralConfig.class, uses = {UtilisateurMapper.class, ReferenceResolver.class})
public interface StockMapper {

    @Mapping(source = "region.code", target = "regionCode")
    ArticleStockResponse toResponse(ArticleStock article);   // statut = getStatut() calculé

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "proprietaire", ignore = true)         // utilisateur connecté
    @Mapping(source = "regionCode", target = "region")
    ArticleStock toEntity(ArticleStockRequest request);

    @InheritConfiguration(name = "toEntity")
    void mettreAJour(ArticleStockRequest request, @MappingTarget ArticleStock article);

    @Mapping(source = "article.id", target = "articleId")
    @Mapping(source = "region.code", target = "regionCode")
    MouvementStockResponse toResponse(MouvementStock mouvement);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "article", ignore = true)              // fixé par le service
    @Mapping(target = "dateMouvement", ignore = true)        // maintenant, fixé par le service
    @Mapping(source = "destinataireId", target = "destinataire")
    @Mapping(source = "regionCode", target = "region")
    MouvementStock toEntity(MouvementStockRequest request);
}