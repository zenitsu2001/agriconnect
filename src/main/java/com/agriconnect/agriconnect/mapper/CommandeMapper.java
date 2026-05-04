package com.agriconnect.agriconnect.mapper;

import com.agriconnect.agriconnect.dto.CommandeRequest;
import com.agriconnect.agriconnect.dto.CommandeResponse;
import com.agriconnect.agriconnect.entity.Commande;
import org.mapstruct.*;

/**
 * Mapper pour l'entité Commande.
 *
 * La Commande est l'entité avec le plus de champs "calculés" :
 * - dateCommande  → fixée par le serveur
 * - prixTotal     → calculé par le service (quantite × prixUnitaire du produit)
 * - statut        → initialisé à EN_ATTENTE par le service
 *
 * Dans le Response, on expose des infos résumées du Client ET du Produit.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommandeMapper {

    /**
     * Request → Entité.
     * clientId et produitId sont des Long dans le DTO.
     * MapStruct crée des objets Client et Produit avec uniquement l'ID renseigné.
     * Le Service devra ensuite les remplacer par les entités complètes chargées depuis la BDD.
     */
    @Mapping(source = "clientId",  target = "client.id")
    @Mapping(source = "produitId", target = "produit.id")
    @Mapping(target = "dateCommande", ignore = true)  // LocalDate.now() dans le Service
    @Mapping(target = "prixTotal",    ignore = true)  // quantite × produit.prixUnitaire dans le Service
    @Mapping(target = "statut",       ignore = true)  // EN_ATTENTE dans le Service
    Commande toEntity(CommandeRequest request);

    /**
     * Entité → Response.
     *
     * On expose depuis le Client : son ID et son nom complet.
     * On expose depuis le Produit : son ID, son nom, son unité et son prix unitaire.
     * Cela permet au front-end d'afficher une ligne de commande complète sans appel supplémentaire.
     */
    @Mapping(source = "client.id",          target = "clientId")
    @Mapping(source = "produit.id",         target = "produitId")
    @Mapping(source = "produit.nom",        target = "produitNom")
    @Mapping(source = "produit.unite",      target = "produitUnite")
    @Mapping(source = "produit.prixUnitaire", target = "produitPrixUnitaire")
    @Mapping(
            expression = "java(commande.getClient().getNom() + \" \" + commande.getClient().getPrenom())",
            target = "clientNomComplet"
    )
    CommandeResponse toResponse(Commande commande);
}