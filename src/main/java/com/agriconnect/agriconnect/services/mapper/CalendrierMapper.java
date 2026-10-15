package com.agriconnect.agriconnect.services.mapper;

import com.agriconnect.agriconnect.entity.calendrier.Evenement;
import com.agriconnect.agriconnect.services.config.CentralConfig;
import com.agriconnect.agriconnect.services.dto.calendrier.request.EvenementRequest;
import com.agriconnect.agriconnect.services.dto.calendrier.response.EvenementResponse;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

// ===== CalendrierMapper.java =====
@Mapper(config = CentralConfig.class)
public interface CalendrierMapper {

    EvenementResponse toResponse(Evenement evenement);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "proprietaire", ignore = true)         // utilisateur connecté
    @Mapping(target = "termine", ignore = true)              // false par défaut (@PrePersist)
    Evenement toEntity(EvenementRequest request);

    @InheritConfiguration(name = "toEntity")
    void mettreAJour(EvenementRequest request, @MappingTarget Evenement evenement);
}