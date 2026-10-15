package com.agriconnect.agriconnect.services.mapper;

import com.agriconnect.agriconnect.entity.calendrier.BaremePrix;
import com.agriconnect.agriconnect.entity.calendrier.CalculPrevente;
import com.agriconnect.agriconnect.services.config.CentralConfig;
import com.agriconnect.agriconnect.services.config.ReferenceResolver;
import com.agriconnect.agriconnect.services.dto.calendrier.request.BaremePrixRequest;
import com.agriconnect.agriconnect.services.dto.calendrier.request.CalculPreventeRequest;
import com.agriconnect.agriconnect.services.dto.calendrier.response.BaremePrixResponse;
import com.agriconnect.agriconnect.services.dto.calendrier.response.CalculPreventeResponse;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

// ===== PreventeMapper.java =====  (calcul des préventes et barèmes de l'État)
@Mapper(config = CentralConfig.class, uses = ReferenceResolver.class)
public interface PreventeMapper {

    @Mapping(source = "produitReference.code", target = "produitCode")
    BaremePrixResponse toResponse(BaremePrix bareme);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "auteur", ignore = true)               // agent de l'État connecté
    @Mapping(source = "produitCode", target = "produitReference")
    BaremePrix toEntity(BaremePrixRequest request);

    @InheritConfiguration(name = "toEntity")
    void mettreAJour(BaremePrixRequest request, @MappingTarget BaremePrix bareme);

    @Mapping(source = "produitReference.code", target = "produitCode")
    @Mapping(source = "dateCreation", target = "dateCalcul")
    CalculPreventeResponse toResponse(CalculPrevente calcul);

    /** Seules les saisies sont mappées : les résultats sont calculés par le service. */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "agriculteur", ignore = true)
    @Mapping(target = "montantBrut", ignore = true)
    @Mapping(target = "montantRemise", ignore = true)
    @Mapping(target = "montantTaxe", ignore = true)
    @Mapping(target = "coutTotal", ignore = true)
    @Mapping(target = "montantFinal", ignore = true)
    @Mapping(target = "beneficeNet", ignore = true)
    @Mapping(target = "margeBeneficiaire", ignore = true)
    @Mapping(target = "prixDansBareme", ignore = true)
    @Mapping(source = "produitCode", target = "produitReference")
    CalculPrevente toEntity(CalculPreventeRequest request);
}
