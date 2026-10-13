package com.agriconnect.agriconnect.repository.marche;

import com.agriconnect.agriconnect.entity.marche.Boost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BoostRepository extends JpaRepository<Boost, Long> {
    boolean existsByProduitIdAndDateFinAfter(Long produitId, LocalDateTime maintenant);
    List<Boost> findByProduitIdOrderByDateFinDesc(Long produitId);
}