package com.agriconnect.agriconnect.mapper;

import com.agriconnect.agriconnect.dto.ClientRequest;
import com.agriconnect.agriconnect.dto.ClientResponse;
import com.agriconnect.agriconnect.entity.Client;
import org.mapstruct.*;

/**
 * Mapper pour l'entité Client.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientMapper {

    @Mapping(target = "motDePasse",      ignore = true)
    @Mapping(target = "dateInscription", ignore = true)
    @Mapping(target = "abonnement",      ignore = true)
    @Mapping(target = "commandes",       ignore = true)
    Client toEntity(ClientRequest request);

    /**
     * "nombreCommandes" est calculé depuis la taille de la liste de commandes.
     * La vérification "!= null" est une sécurité indispensable :
     * si la liste n'a jamais été initialisée (objet récupéré partiellement),
     * un NullPointerException serait lancé sans ce guard.
     */
    @Mapping(
            expression = "java(client.getCommandes() != null ? client.getCommandes().size() : 0)",
            target = "nombreCommandes"
    )
    ClientResponse toResponse(Client client);
}