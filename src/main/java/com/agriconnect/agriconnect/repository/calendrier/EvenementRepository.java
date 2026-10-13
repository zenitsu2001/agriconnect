package com.agriconnect.agriconnect.repository.calendrier;

import com.agriconnect.agriconnect.entity.calendrier.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;

public interface EvenementRepository extends JpaRepository<Evenement, Long> {

    List<Evenement> findByProprietaireIdAndDateEvenementBetweenOrderByDateEvenement(
            Long proprietaireId, LocalDate debut, LocalDate fin);              // affichage d'un mois

    List<Evenement> findByProprietaireIdAndDateEvenementGreaterThanEqualAndTermineFalseOrderByDateEvenement(
            Long proprietaireId, LocalDate depuis, Pageable pageable);         // « Événements à venir »
}