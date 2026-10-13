package com.agriconnect.agriconnect.repository.calendrier;

import com.agriconnect.agriconnect.entity.calendrier.BaremePrix;
import com.agriconnect.agriconnect.enums.Unite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BaremePrixRepository extends JpaRepository<BaremePrix, Long> {
    Optional<BaremePrix> findByProduitReferenceCodeAndUnite(String codeProduit, Unite unite);
    List<BaremePrix> findAllByOrderByProduitReferenceNomAsc();
}