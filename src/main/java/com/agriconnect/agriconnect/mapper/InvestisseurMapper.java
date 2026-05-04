package com.agriconnect.agriconnect.mapper;

import com.agriconnect.agriconnect.dto.InvestisseurRequest;
import com.agriconnect.agriconnect.dto.InvestisseurResponse;
import com.agriconnect.agriconnect.entity.Investisseur;
import org.mapstruct.*;

/**
 * Mapper pour l'entité Investisseur.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InvestisseurMapper {

    @Mapping(target = "motDePasse",        ignore = true)
    @Mapping(target = "dateInscription",   ignore = true)
    @Mapping(target = "abonnement",        ignore = true)
    @Mapping(target = "offres",            ignore = true)
    @Mapping(target = "messagesEnvoyes",   ignore = true)
    @Mapping(target = "messagesRecus",     ignore = true)
    Investisseur toEntity(InvestisseurRequest request);

    /**
     * "nombreOffres" : on compte uniquement les offres de cet investisseur.
     * Les listes messagesEnvoyes et messagesRecus ne sont volontairement pas
     * exposées dans le Response — elles ont leur propre endpoint dédié.
     */
    @Mapping(
            expression = "java(investisseur.getOffres() != null ? investisseur.getOffres().size() : 0)",
            target = "nombreOffres"
    )
    InvestisseurResponse toResponse(Investisseur investisseur);
}