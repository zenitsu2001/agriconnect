package com.agriconnect.agriconnect.repository.abonnement;

import com.agriconnect.agriconnect.entity.abonnement.Abonnement;
import com.agriconnect.agriconnect.enums.StatutAbonnement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AbonnementRepository extends JpaRepository<Abonnement, Long> {
    Optional<Abonnement> findFirstByUtilisateurIdAndStatutOrderByDateFinDesc(Long utilisateurId, StatutAbonnement statut);
    List<Abonnement> findByStatutAndDateFinBefore(StatutAbonnement statut, LocalDateTime date);   // tâche d'expiration

    Optional<Abonnement> findByPaiementId(Long paiementId);
    boolean existsByUtilisateurIdAndStatut(Long utilisateurId, StatutAbonnement statut);

    /** Fin la plus lointaine des abonnements actifs : le nouvel abonnement payé commence à ce moment. */
    @Query("""
        select max(a.dateFin) from Abonnement a
        where a.utilisateur.id = :utilisateurId
          and a.statut = com.agriconnect.agriconnect.enums.StatutAbonnement.ACTIF
          and a.dateFin is not null and a.dateFin > :maintenant
        """)
    Optional<LocalDateTime> finLaPlusLointaine(@Param("utilisateurId") Long utilisateurId,
                                               @Param("maintenant") LocalDateTime maintenant);

    /** Abonnements en vigueur maintenant, le plus cher d'abord. */
    @Query("""
        select a from Abonnement a join fetch a.plan p
        where a.utilisateur.id = :utilisateurId
          and a.statut = com.agriconnect.agriconnect.enums.StatutAbonnement.ACTIF
          and a.dateDebut <= :maintenant
          and (a.dateFin is null or a.dateFin > :maintenant)
        order by p.prix desc
        """)
    List<Abonnement> trouverEnCours(@Param("utilisateurId") Long utilisateurId,
                                    @Param("maintenant") LocalDateTime maintenant, Pageable pageable);
}