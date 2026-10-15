package com.agriconnect.agriconnect.services.dto.messagerie.response;

import com.agriconnect.agriconnect.services.dto.metier.response.UtilisateurResume;

import java.time.LocalDateTime;

public record ConversationResponse(Long id, UtilisateurResume contact, String dernierMessage,
                                   boolean dernierMessageVocal, LocalDateTime derniereActivite,
                                   long nonLus, boolean enLigne) {}