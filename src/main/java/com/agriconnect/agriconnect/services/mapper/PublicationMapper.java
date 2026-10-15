package com.agriconnect.agriconnect.services.mapper;

import com.agriconnect.agriconnect.entity.messagerie.Notification;
import com.agriconnect.agriconnect.entity.messagerie.Publication;
import com.agriconnect.agriconnect.entity.parametre.Region;
import com.agriconnect.agriconnect.services.config.CentralConfig;
import com.agriconnect.agriconnect.services.config.ReferenceResolver;
import com.agriconnect.agriconnect.services.dto.messagerie.request.PublicationRequest;
import com.agriconnect.agriconnect.services.dto.messagerie.response.NotificationResponse;
import com.agriconnect.agriconnect.services.dto.messagerie.response.PublicationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// ===== PublicationMapper.java =====  (publications de l'État et notifications)
@Mapper(config = CentralConfig.class, uses = {UtilisateurMapper.class, ReferenceResolver.class})
public interface PublicationMapper {

    @Mapping(source = "regionsCibles", target = "regionsCodes")
    PublicationResponse toResponse(Publication publication);

    default String code(Region region) {                     // Set<Region> -> List<String>
        return region == null ? null : region.getCode();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "auteur", ignore = true)               // agent de l'État connecté
    @Mapping(target = "datePublication", ignore = true)      // fixée à la publication effective
    @Mapping(source = "publier", target = "publie")
    @Mapping(source = "regionsCodes", target = "regionsCibles")   // codes -> Region via ReferenceResolver
    Publication toEntity(PublicationRequest request);

    NotificationResponse toResponse(Notification notification);
}