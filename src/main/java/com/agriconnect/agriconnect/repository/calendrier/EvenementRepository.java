package com.agriconnect.agriconnect.repository.calendrier;

import com.agriconnect.agriconnect.entity.calendrier.Evenement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EvenementRepository extends JpaRepository<Evenement, Long> {

    List<Evenement> findByProprietaireIdAndDateEvenementBetweenOrderByDateEvenement(
            Long proprietaireId, LocalDate debut, LocalDate fin);              // affichage d'un mois

    List<Evenement> findByProprietaireIdAndDateEvenementGreaterThanEqualAndTermineFalseOrderByDateEvenement(
            Long proprietaireId, LocalDate depuis, Pageable pageable);         // « Événements à venir »

    // EvenementRepository.java : ajouter
    /** Retrouve un événement SEULEMENT s'il appartient à cet agriculteur (contrôle d'accès). */
    Optional<Evenement> findByIdAndProprietaireId(Long id, Long proprietaireId);

    /**
     * Rappels : événements non terminés entre deux dates, qui sont soit de priorité HAUTE,
     * soit de type RECOLTE ou IRRIGATION. Tri : date, puis priorité (haute d'abord).
     * Le Pageable sert uniquement à limiter le nombre de résultats.
     */
    @Query("""
        select e from Evenement e
        where e.proprietaire.id = :proprietaireId
          and e.termine = false
          and e.dateEvenement between :debut and :fin
          and (e.priorite = com.agriconnect.agriconnect.enums.Priorite.HAUTE
               or e.type in (com.agriconnect.agriconnect.enums.TypeEvenement.RECOLTE,
                             com.agriconnect.agriconnect.enums.TypeEvenement.IRRIGATION))
        order by e.dateEvenement asc,
                 case e.priorite
                      when com.agriconnect.agriconnect.enums.Priorite.HAUTE then 0
                      when com.agriconnect.agriconnect.enums.Priorite.MOYENNE then 1
                      else 2 end asc,
                 e.id asc
        """)
    List<Evenement> trouverRappels(@Param("proprietaireId") Long proprietaireId,
                                   @Param("debut") LocalDate debut,
                                   @Param("fin") LocalDate fin,
                                   Pageable pageable);
}