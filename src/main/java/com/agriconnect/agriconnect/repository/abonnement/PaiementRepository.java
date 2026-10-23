package com.agriconnect.agriconnect.repository.abonnement;

import com.agriconnect.agriconnect.entity.abonnement.Paiement;
import com.agriconnect.agriconnect.enums.StatutPaiement;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    Optional<Paiement> findByReferenceExterne(String referenceExterne);       // retour du prestataire (webhook)
    List<Paiement> findByUtilisateurIdOrderByDateCreationDesc(Long utilisateurId);
    // PaiementRepository.java : ajouter
    @Lock(LockModeType.PESSIMISTIC_WRITE)       // verrou : webhook et scrutation ne peuvent pas confirmer en même temps
    @Query("select p from Paiement p where p.reference = :reference")
    Optional<Paiement> verrouillerParReference(@Param("reference") String reference);

    List<Paiement> findByStatutAndDateCreationBetween(StatutPaiement statut, LocalDateTime debut, LocalDateTime fin);
    List<Paiement> findByStatutAndDateCreationBefore(StatutPaiement statut, LocalDateTime limite);
}