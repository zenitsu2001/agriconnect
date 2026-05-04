package com.agriconnect.agriconnect.mapper;

import com.agriconnect.agriconnect.dto.ProduitRequest;
import com.agriconnect.agriconnect.dto.ProduitResponse;
import com.agriconnect.agriconnect.entity.Produit;
import org.mapstruct.*;

/**
 * Mapper pour l'entité Produit.
 *
 * Cas intéressant : le DTO de réponse contient 3 champs sur l'agriculteur
 * (agriculteurId, agriculteurNomComplet, agriculteurLocalisation)
 * qui viennent tous de l'objet imbriqué produit.getAgriculteur().
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProduitMapper {

    /**
     * Request → Entité.
     *
     * @Mapping(source = "agriculteurId", target = "agriculteur.id") :
     *   Le DTO envoie un simple Long, l'entité attend un objet Agriculteur.
     *   MapStruct crée un objet Agriculteur vide et y place l'ID.
     *   Le Service doit ensuite appeler agriculteurRepository.findById() pour
     *   charger l'Agriculteur complet et l'associer au Produit.
     */
    @Mapping(source = "agriculteurId", target = "agriculteur.id")
    @Mapping(target = "datePublication", ignore = true)  // Fixée par le serveur
    @Mapping(target = "booste",          ignore = true)  // false par défaut, géré par le service
    @Mapping(target = "commandes",       ignore = true)
    Produit toEntity(ProduitRequest request);

    /**
     * Entité → Response.
     *
     * Les 3 champs agriculteur sont extraits via la notation pointée "agriculteur.xxx"
     * ou via des expressions Java pour les champs calculés.
     *
     * "agriculteur.id"          → agriculteurId           (navigation simple)
     * "agriculteur.localisation"→ agriculteurLocalisation  (navigation simple)
     * nom + " " + prenom        → agriculteurNomComplet    (concaténation → expression Java)
     */
    @Mapping(source = "agriculteur.id",          target = "agriculteurId")
    @Mapping(source = "agriculteur.localisation", target = "agriculteurLocalisation")
    @Mapping(
            expression = "java(produit.getAgriculteur().getNom() + \" \" + produit.getAgriculteur().getPrenom())",
            target = "agriculteurNomComplet"
    )
    ProduitResponse toResponse(Produit produit);
}