package com.agriconnect.agriconnect.dto;

import com.agriconnect.agriconnect.enums.TypeMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO renvoyé lors de la lecture d'un message ou d'une conversation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private Long id;
    private String contenu;
    private TypeMessage typeMessage;
    private LocalDateTime dateEnvoi;

    /**
     * Indique si le destinataire a lu le message.
     * Utile pour afficher les indicateurs "lu / non lu" dans l'interface.
     */
    private Boolean lu;

    private String fichierAudio;

    // Résumé de l'expéditeur
    private Long expediteurId;
    private String expediteurNomComplet;

    // Résumé du destinataire
    private Long destinataireId;
    private String destinataireNomComplet;
}