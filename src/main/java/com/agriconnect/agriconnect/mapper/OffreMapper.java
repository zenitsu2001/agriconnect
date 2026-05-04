package com.agriconnect.agriconnect.mapper;

import com.agriconnect.agriconnect.dto.OffreRequest;
import com.agriconnect.agriconnect.dto.OffreResponse;
import com.agriconnect.agriconnect.entity.Offre;
import org.mapstruct.*;

/**
 * Mapper pour l'entité Offre.
 *
 * Offre est le cas le plus délicat car elle a DEUX relations optionnelles :
 * - soit liée à un Investisseur (investisseur != null, partenaire == null)
 * - soit liée à un Partenaire   (partenaire != null, investisseur == null)
 *
 * MapStruct gérera les null automatiquement : si investisseur est null,
 * investisseurId et investisseurNomComplet seront null dans le Response — ce qui est correct.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OffreMapper {

    /**
     * Request → Entité.
     * On mappe les deux IDs optionnels vers leurs objets imbriqués respectifs.
     * datePublication est fixée par le Service (LocalDate.now()).
     * booste est false par défaut, activé uniquement par un Admin.
     */
    @Mapping(source = "investisseurId", target = "investisseur.id")
    @Mapping(source = "partenaireId",   target = "partenaire.id")
    @Mapping(target = "datePublication", ignore = true)
    @Mapping(target = "booste",          ignore = true)
    Offre toEntity(OffreRequest request);

    /**
     * Entité → Response.
     *
     * Pour chaque publieur (investisseur ou partenaire), on extrait l'ID
     * et le nom complet. Si l'un des deux est null, les expressions Java
     * pourraient lancer un NullPointerException.
     *
     * On utilise donc une expression ternaire sécurisée :
     *   offre.getInvestisseur() != null ? nom + prénom : null
     *
     * C'est le même principe que le "null-safe operator" dans d'autres langages.
     */
    @Mapping(source = "investisseur.id", target = "investisseurId")
    @Mapping(source = "partenaire.id",   target = "partenaireId")
    @Mapping(
            expression = "java(offre.getInvestisseur() != null " +
                    "? offre.getInvestisseur().getNom() + \" \" + offre.getInvestisseur().getPrenom() " +
                    ": null)",
            target = "investisseurNomComplet"
    )
    @Mapping(
            expression = "java(offre.getPartenaire() != null " +
                    "? offre.getPartenaire().getNom() + \" \" + offre.getPartenaire().getPrenom() " +
                    ": null)",
            target = "partenaireNomComplet"
    )
    OffreResponse toResponse(Offre offre);
}