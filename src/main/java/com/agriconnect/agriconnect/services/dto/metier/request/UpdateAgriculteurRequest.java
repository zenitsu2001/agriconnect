package com.agriconnect.agriconnect.services.dto.metier.request;

import com.agriconnect.agriconnect.services.dto.parametrage.request.LocalisationRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

/** Exploitation de l'agriculteur : alimente le calendrier et les alertes météo. */
public record UpdateAgriculteurRequest(@Size(max = 150) String nomExploitation,
                                       @Valid LocalisationRequest localisation) {}
