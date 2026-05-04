package com.agriconnect.agriconnect.mapper;

import com.agriconnect.agriconnect.dto.AdminRequest;
import com.agriconnect.agriconnect.dto.AdminResponse;
import com.agriconnect.agriconnect.entity.Admin;
import org.mapstruct.*;

/**
 * Mapper pour l'entité Admin.
 *
 * Admin hérite de Utilisateur : les champs comme nom, prenom, email
 * sont physiquement dans la classe parente, mais MapStruct les voit
 * tous à travers l'héritage — aucune configuration spéciale n'est nécessaire.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminMapper {

    /**
     * Request → Entité.
     *
     * @Mapping(target = "motDePasse", ignore = true) :
     *   On ne stocke JAMAIS le mot de passe en clair dans l'entité.
     *   Le Service récupérera request.getMotDePasse(), le hashera avec BCrypt,
     *   puis appelera admin.setMotDePasse(hash). Le Mapper ne touche pas à ça.
     *
     * @Mapping(target = "dateInscription", ignore = true) :
     *   La date d'inscription est fixée par le Service (LocalDate.now()),
     *   pas par le client.
     *
     * @Mapping(target = "abonnement", ignore = true) :
     *   La relation avec Abonnement est gérée séparément après la création du compte.
     */
    @Mapping(target = "motDePasse",      ignore = true)
    @Mapping(target = "dateInscription", ignore = true)
    @Mapping(target = "abonnement",      ignore = true)
    Admin toEntity(AdminRequest request);

    /**
     * Entité → Response.
     * motDePasse est absent du DTO AdminResponse donc MapStruct l'ignore naturellement.
     * On n'a rien à préciser ici : tous les champs se mappent directement par leur nom.
     */
    AdminResponse toResponse(Admin admin);
}