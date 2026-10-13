package com.agriconnect.agriconnect.repository.abonnement;

import com.agriconnect.agriconnect.entity.abonnement.Abonnement;
import com.agriconnect.agriconnect.enums.StatutAbonnement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AbonnementRepository extends JpaRepository<Abonnement, Long> {
    Optional<Abonnement> findFirstByUtilisateurIdAndStatutOrderByDateFinDesc(Long utilisateurId, StatutAbonnement statut);
    List<Abonnement> findByStatutAndDateFinBefore(StatutAbonnement statut, LocalDateTime date);   // tâche d'expiration
}