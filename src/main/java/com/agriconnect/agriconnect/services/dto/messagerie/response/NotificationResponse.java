package com.agriconnect.agriconnect.services.dto.messagerie.response;

import java.time.LocalDateTime;

public record NotificationResponse(Long id, String type, String titre, String message, String lien,
                                   boolean lue, LocalDateTime dateCreation) {}
