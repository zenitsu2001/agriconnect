package com.agriconnect.agriconnect.mapper;

import com.agriconnect.agriconnect.dto.PartenaireRequest;
import com.agriconnect.agriconnect.dto.PartenaireResponse;
import com.agriconnect.agriconnect.entity.Partenaire;
import org.mapstruct.*;

/**
 * Mapper pour l'entité Partenaire.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PartenaireMapper {

    @Mapping(target = "motDePasse",      ignore = true)
    @Mapping(target = "dateInscription", ignore = true)
    @Mapping(target = "abonnement",      ignore = true)
    @Mapping(target = "offres",          ignore = true)
    Partenaire toEntity(PartenaireRequest request);

    @Mapping(
            expression = "java(partenaire.getOffres() != null ? partenaire.getOffres().size() : 0)",
            target = "nombreOffres"
    )
    PartenaireResponse toResponse(Partenaire partenaire);
}