package com.agriconnect.agriconnect.services.dto.marche.response;

import com.agriconnect.agriconnect.services.dto.abonnement.response.PaiementInitieResponse;

// BoostCommandeResponse.java : paiement = null si le boost est gratuit (activé immédiatement)
public record BoostCommandeResponse(BoostResponse boost, PaiementInitieResponse paiement) {}