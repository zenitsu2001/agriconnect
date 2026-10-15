package com.agriconnect.agriconnect.services.dto.messagerie.response;

import java.time.LocalDateTime;

public record MessageResponse(Long id, Long conversationId, String contenu, String audioUrl,
                              Integer dureeAudioSecondes, String pieceJointeUrl, String pieceJointeNom,
                              LocalDateTime dateEnvoi, boolean mien, boolean lu) {}

