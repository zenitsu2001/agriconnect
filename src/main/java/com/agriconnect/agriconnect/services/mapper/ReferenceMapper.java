package com.agriconnect.agriconnect.services.mapper;

import com.agriconnect.agriconnect.entity.marche.ProduitReference;
import com.agriconnect.agriconnect.entity.parametre.*;
import com.agriconnect.agriconnect.services.config.CentralConfig;
import com.agriconnect.agriconnect.services.config.ReferenceResolver;
import com.agriconnect.agriconnect.services.dto.marche.ProduitReferenceDto;
import com.agriconnect.agriconnect.services.dto.parametrage.*;
import com.agriconnect.agriconnect.services.dto.parametrage.request.LocalisationRequest;
import org.mapstruct.*;
import java.util.List;

// ===== ReferenceMapper.java =====  (tables de paramétrage et localisation)
@Mapper(config = CentralConfig.class, uses = ReferenceResolver.class)
public interface ReferenceMapper {

    RegionDto toDto(Region region);
    List<RegionDto> toRegionDtos(List<Region> regions);

    @Mapping(source = "region.code", target = "regionCode")
    DepartementDto toDto(Departement departement);
    List<DepartementDto> toDepartementDtos(List<Departement> departements);

    @Mapping(source = "departement.id", target = "departementId")
    CommuneDto toDto(Commune commune);
    List<CommuneDto> toCommuneDtos(List<Commune> communes);

    LangueDto toDto(LangueSupportee langue);
    List<LangueDto> toLangueDtos(List<LangueSupportee> langues);

    ProduitReferenceDto toDto(ProduitReference produitReference);
    List<ProduitReferenceDto> toProduitReferenceDtos(List<ProduitReference> produitsReference);

    @Mapping(source = "region.code", target = "regionCode")
    @Mapping(source = "departement.id", target = "departementId")
    @Mapping(source = "departement.nom", target = "departementNom")
    @Mapping(source = "commune.id", target = "communeId")
    @Mapping(source = "commune.nom", target = "communeNom")
    LocalisationDto toDto(Localisation localisation);

    @Mapping(source = "regionCode", target = "region")           // via ReferenceResolver
    @Mapping(source = "departementId", target = "departement")
    @Mapping(source = "communeId", target = "commune")
    Localisation toEntity(LocalisationRequest request);
}