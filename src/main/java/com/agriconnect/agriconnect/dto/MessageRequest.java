package com.agriconnect.agriconnect.dto;

import com.agriconnect.agriconnect.enums.TypeMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO reçu quand un Investisseur envoie un message à un autre.
 *
 * Note : dateEnvoi et lu sont gérés par le serveur :
 * - dateEnvoi est toujours LocalDateTime.now() au moment de l'envoi
 * - lu est toujours false à la création (le destinataire n'a pas encore lu)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {

    @NotBlank(message = "Le contenu du message est obligatoire")
    private String contenu;

    /**
     * @NotNull : on doit savoir si c'est un message TEXTE ou AUDIO.
     */
    @NotNull(message = "Le type de message est obligatoire")
    private TypeMessage typeMessage;

    /**
     * Chemin vers le fichier audio (uniquement si typeMessage = AUDIO).
     * Peut être null pour un message TEXTE.
     */
    private String fichierAudio;

    @NotNull(message = "L'identifiant de l'expéditeur est obligatoire")
    private Long expediteurId;

    @NotNull(message = "L'identifiant du destinataire est obligatoire")
    private Long destinataireId;
}