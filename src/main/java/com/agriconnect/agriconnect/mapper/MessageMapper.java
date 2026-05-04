package com.agriconnect.agriconnect.mapper;

import com.agriconnect.agriconnect.dto.MessageRequest;
import com.agriconnect.agriconnect.dto.MessageResponse;
import com.agriconnect.agriconnect.entity.Message;
import org.mapstruct.*;

/**
 * Mapper pour l'entité Message.
 *
 * Message a deux relations vers Investisseur (expéditeur et destinataire).
 * On doit mapper chacune séparément avec la notation pointée.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageMapper {

    /**
     * Request → Entité.
     *
     * expediteurId  → expediteur.id   (crée un Investisseur avec juste l'ID)
     * destinataireId→ destinataire.id  (idem)
     *
     * dateEnvoi est ignoré : le Service le fixe à LocalDateTime.now() au moment de l'envoi.
     * lu est ignoré : toujours false à la création (message non lu par défaut).
     */
    @Mapping(source = "expediteurId",   target = "expediteur.id")
    @Mapping(source = "destinataireId", target = "destinataire.id")
    @Mapping(target = "dateEnvoi",      ignore = true)
    @Mapping(target = "lu",             ignore = true)
    Message toEntity(MessageRequest request);

    /**
     * Entité → Response.
     *
     * Pour l'expéditeur et le destinataire, on extrait :
     *   - l'ID via navigation simple (source = "expediteur.id")
     *   - le nom complet via expression Java (concaténation nom + prénom)
     */
    @Mapping(source = "expediteur.id",   target = "expediteurId")
    @Mapping(source = "destinataire.id", target = "destinataireId")
    @Mapping(
            expression = "java(message.getExpediteur().getNom() + \" \" + message.getExpediteur().getPrenom())",
            target = "expediteurNomComplet"
    )
    @Mapping(
            expression = "java(message.getDestinataire().getNom() + \" \" + message.getDestinataire().getPrenom())",
            target = "destinataireNomComplet"
    )
    MessageResponse toResponse(Message message);
}