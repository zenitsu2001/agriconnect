package com.agriconnect.agriconnect.repository.marche;

import com.agriconnect.agriconnect.entity.marche.ProduitReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProduitReferenceRepository extends JpaRepository<ProduitReference, Long> {
    Optional<ProduitReference> findByCode(String code);
}