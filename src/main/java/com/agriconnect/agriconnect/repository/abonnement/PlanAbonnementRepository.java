package com.agriconnect.agriconnect.repository.abonnement;


import com.agriconnect.agriconnect.entity.abonnement.PlanAbonnement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanAbonnementRepository extends JpaRepository<PlanAbonnement, Long> {
    Optional<PlanAbonnement> findByCode(String code);
    List<PlanAbonnement> findByActifTrueOrderByOrdreAffichageAsc();
}