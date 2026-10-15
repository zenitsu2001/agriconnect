package com.agriconnect.agriconnect.services.dto.messagerie.request;

import jakarta.validation.constraints.NotNull;

public record NouvelleConversationRequest(@NotNull Long contactId) {}
