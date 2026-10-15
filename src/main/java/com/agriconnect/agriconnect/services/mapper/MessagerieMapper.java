package com.agriconnect.agriconnect.services.mapper;

import com.agriconnect.agriconnect.entity.messagerie.Conversation;
import com.agriconnect.agriconnect.entity.messagerie.Message;
import com.agriconnect.agriconnect.entity.metier.Utilisateur;
import com.agriconnect.agriconnect.services.config.CentralConfig;
import com.agriconnect.agriconnect.services.dto.messagerie.request.MessageRequest;
import com.agriconnect.agriconnect.services.dto.messagerie.response.ConversationResponse;
import com.agriconnect.agriconnect.services.dto.messagerie.response.MessageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// ===== MessagerieMapper.java =====
@Mapper(config = CentralConfig.class, uses = UtilisateurMapper.class)
public interface MessagerieMapper {

    /** `mien` : le message a été écrit par l'utilisateur connecté. */
    @Mapping(source = "message.conversation.id", target = "conversationId")
    @Mapping(target = "mien", expression = "java(message.getExpediteur().getId().equals(utilisateurId))")
    MessageResponse toResponse(Message message, Long utilisateurId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "conversation", ignore = true)
    @Mapping(target = "expediteur", ignore = true)           // utilisateur connecté
    @Mapping(target = "dateEnvoi", ignore = true)
    @Mapping(target = "lu", ignore = true)
    Message toEntity(MessageRequest request);

    /** Le contact, le dernier message, les non-lus et la présence sont fournis par le service. */
    @Mapping(source = "conversation.id", target = "id")
    @Mapping(source = "conversation.derniereActivite", target = "derniereActivite")
    @Mapping(source = "contact", target = "contact")
    @Mapping(source = "dernier.contenu", target = "dernierMessage")
    @Mapping(target = "dernierMessageVocal", expression = "java(dernier != null && dernier.getAudioUrl() != null)")
    @Mapping(source = "nonLus", target = "nonLus")
    @Mapping(source = "enLigne", target = "enLigne")
    ConversationResponse toResponse(Conversation conversation, Utilisateur contact, Message dernier,
                                    long nonLus, boolean enLigne);

    default Utilisateur autreParticipant(Conversation conversation, Long utilisateurId) {
        return conversation.getParticipants().stream()
                .filter(u -> !u.getId().equals(utilisateurId))
                .findFirst().orElse(null);
    }
}