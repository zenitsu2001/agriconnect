package com.agriconnect.agriconnect.services.dto.messagerie.response;

import com.agriconnect.agriconnect.services.dto.metier.response.UtilisateurResume;

import java.time.LocalDateTime;
import java.util.List;

public record PublicationResponse(Long id, String titre, String contenu, String categorie, boolean publie,
                                  LocalDateTime datePublication, boolean notifierAgriculteurs,
                                  List<String> regionsCodes, UtilisateurResume auteur) {}


