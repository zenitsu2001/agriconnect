package com.agriconnect.agriconnect.services.mapper;

import com.agriconnect.agriconnect.entity.marche.Avis;
import com.agriconnect.agriconnect.entity.marche.Boost;
import com.agriconnect.agriconnect.entity.marche.Commande;
import com.agriconnect.agriconnect.entity.marche.Produit;
import com.agriconnect.agriconnect.services.config.CentralConfig;
import com.agriconnect.agriconnect.services.config.ReferenceResolver;
import com.agriconnect.agriconnect.services.dto.marche.request.AvisRequest;
import com.agriconnect.agriconnect.services.dto.marche.request.CommandeRequest;
import com.agriconnect.agriconnect.services.dto.marche.request.ProduitRequest;
import com.agriconnect.agriconnect.services.dto.marche.response.*;
import com.agriconnect.agriconnect.services.mapper.UtilisateurMapper;
import org.mapstruct.*;

// ===== ProduitMapper.java =====
@Mapper(config = CentralConfig.class, uses = {UtilisateurMapper.class, ReferenceResolver.class})
public interface ProduitMapper {

    /** Les statistiques (note moyenne, boost en cours) viennent de requêtes du service, pas de l'entité. */
    @Mapping(source = "produit.agriculteur", target = "agriculteur")                     // -> UtilisateurResume
    @Mapping(source = "produit.agriculteur.exploitation.libelle", target = "localisation")
    @Mapping(source = "produit.agriculteur.exploitation.region.code", target = "regionCode")
    @Mapping(source = "produit.produitReference.code", target = "produitReferenceCode")
    ProduitResponse toResponse(Produit produit, ProduitStats stats);

    @Mapping(target = "imagePrincipale",
            expression = "java(produit.getImages() == null || produit.getImages().isEmpty() ? null : produit.getImages().get(0))")
    ProduitResume toResume(Produit produit);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "agriculteur", ignore = true)         // utilisateur connecté, fixé par le service
    @Mapping(target = "statut", ignore = true)              // EN_VENTE par défaut (@PrePersist)
    @Mapping(target = "nombreVues", ignore = true)
    @Mapping(source = "produitReferenceCode", target = "produitReference")
    Produit toEntity(ProduitRequest request);

    @InheritConfiguration(name = "toEntity")
    @Mapping(target = "description", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "emoji", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    void mettreAJour(ProduitRequest request, @MappingTarget Produit produit);

    BoostResponse toResponse(Boost boost);

    @Mapping(source = "dateCreation", target = "date")
    AvisResponse toResponse(Avis avis);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "produit", ignore = true)
    @Mapping(target = "auteur", ignore = true)
    Avis toEntity(AvisRequest request);

    CommandeResponse toResponse(Commande commande);         // produit -> ProduitResume, acheteur -> UtilisateurResume

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "acheteur", ignore = true)            // utilisateur connecté
    @Mapping(target = "prixUnitaire", ignore = true)        // figés par le service à partir du produit
    @Mapping(target = "montantTotal", ignore = true)
    @Mapping(target = "statut", ignore = true)              // EN_ATTENTE par défaut
    @Mapping(source = "produitId", target = "produit")      // via ReferenceResolver
    Commande toEntity(CommandeRequest request);
}