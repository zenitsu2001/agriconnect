package com.agriconnect.agriconnect.mapper;

import com.agriconnect.agriconnect.dto.AbonnementRequest;
import com.agriconnect.agriconnect.dto.AbonnementResponse;
import com.agriconnect.agriconnect.entity.Abonnement;
import org.mapstruct.*;

/**
 * Mapper MapStruct pour l'entité Abonnement.
 *
 * @Mapper : indique à MapStruct de générer une implémentation pour cette interface.
 *
 * componentModel = "spring" : la classe générée sera un @Component Spring.
 *   → On pourra l'injecter avec @Autowired ou via le constructeur dans nos Services.
 *   → Sans ça, MapStruct génère une classe simple sans injection possible.
 *
 * unmappedTargetPolicy = ReportingPolicy.IGNORE : si un champ de la cible
 *   n'a pas de correspondance dans la source, MapStruct l'ignore silencieusement
 *   au lieu de générer un avertissement de compilation.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AbonnementMapper {

    /**
     * Convertit un AbonnementRequest (DTO entrant) en entité Abonnement.
     *
     * POURQUOI on ignore "utilisateur" complètement ici ?
     * Utilisateur est une classe ABSTRAITE — MapStruct ne peut pas l'instancier
     * car il ne sait pas quelle sous-classe créer (Admin ? Agriculteur ? Client ?).
     * C'est donc le SERVICE qui s'en chargera :
     *   1. Il appellera toEntity(request) pour créer l'Abonnement de base
     *   2. Il chargera l'utilisateur depuis la BDD via utilisateurRepository.findById(request.getUtilisateurId())
     *   3. Il appellera abonnement.setUtilisateur(utilisateurCharge)
     */
    @Mapping(target = "utilisateur", ignore = true)
    @Mapping(target = "statut",      ignore = true)
    Abonnement toEntity(AbonnementRequest request);

    /**
     * Convertit une entité Abonnement en AbonnementResponse (DTO sortant).
     *
     * source = "utilisateur.id"    → navigue dans l'objet imbriqué Utilisateur pour récupérer l'ID
     * target = "utilisateurId"     → le place dans le champ plat du DTO
     *
     * L'expression Spring Expression Language (expression = "java(...)")
     * permet de concaténer nom + prénom en une seule chaîne.
     */
    @Mapping(source = "utilisateur.id",     target = "utilisateurId")
    @Mapping(
            expression = "java(abonnement.getUtilisateur().getNom() + \" \" + abonnement.getUtilisateur().getPrenom())",
            target = "utilisateurNomComplet"
    )
    AbonnementResponse toResponse(Abonnement abonnement);
}