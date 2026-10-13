package com.agriconnect.agriconnect.repository.abonnement;

import com.agriconnect.agriconnect.entity.abonnement.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    Optional<Paiement> findByReferenceExterne(String referenceExterne);       // retour du prestataire (webhook)
    List<Paiement> findByUtilisateurIdOrderByDateCreationDesc(Long utilisateurId);
}