package com.agriconnect.agriconnect.mapper;

import com.agriconnect.agriconnect.dto.AgriculteurRequest;
import com.agriconnect.agriconnect.dto.AgriculteurResponse;
import com.agriconnect.agriconnect.entity.Agriculteur;
import org.mapstruct.*;

/**
 * Mapper pour l'entité Agriculteur.
 *
 * Particularité : le DTO de réponse contient "nombreProduits" (un entier calculé)
 * qui n'existe pas comme champ dans l'entité — on doit le calculer via une expression.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AgriculteurMapper {

    /**
     * Request → Entité.
     * Même logique que AdminMapper : on exclut les champs gérés par le serveur.
     */
    @Mapping(target = "motDePasse",      ignore = true)
    @Mapping(target = "dateInscription", ignore = true)
    @Mapping(target = "abonnement",      ignore = true)
    @Mapping(target = "produits",        ignore = true)  // La liste de produits se construit séparément
    @Mapping(target = "stocks",          ignore = true)
    Agriculteur toEntity(AgriculteurRequest request);

    /**
     * Entité → Response.
     *
     * "nombreProduits" est calculé avec une expression Java directement dans l'annotation.
     * agriculteur.getProduits() retourne la liste — .size() donne le nombre.
     *
     * Attention : cela déclenche un chargement LAZY de la liste si la session JPA
     * est encore ouverte. Si tu obtiens une LazyInitializationException en test,
     * il faudra soit utiliser EAGER, soit calculer ce champ dans le Service avant
     * d'appeler le Mapper.
     */
    @Mapping(
            expression = "java(agriculteur.getProduits() != null ? agriculteur.getProduits().size() : 0)",
            target = "nombreProduits"
    )
    AgriculteurResponse toResponse(Agriculteur agriculteur);
}