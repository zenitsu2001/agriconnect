package com.agriconnect.agriconnect.services.dto.messagerie.request;

public record MessageRequest(String contenu, String audioUrl, Integer dureeAudioSecondes,
                             String pieceJointeUrl, String pieceJointeNom) {}

