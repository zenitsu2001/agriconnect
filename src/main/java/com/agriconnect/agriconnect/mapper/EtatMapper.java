package com.agriconnect.agriconnect.mapper;

import com.agriconnect.agriconnect.dto.EtatRequest;
import com.agriconnect.agriconnect.dto.EtatResponse;
import com.agriconnect.agriconnect.entity.Etat;
import org.mapstruct.*;

/**
 * Mapper pour l'entité Etat.
 * Etat est le type de compte le plus simple : pas de listes imbriquées,
 * pas de champs calculés — uniquement des champs scalaires (String, LocalDate...).
 * MapStruct les mappe tous automatiquement par correspondance de noms.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EtatMapper {

    @Mapping(target = "motDePasse",      ignore = true)
    @Mapping(target = "dateInscription", ignore = true)
    @Mapping(target = "abonnement",      ignore = true)
    Etat toEntity(EtatRequest request);

    // Tous les champs scalaires se mappent directement → aucune annotation supplémentaire
    EtatResponse toResponse(Etat etat);
}