package com.agriconnect.agriconnect.repository.marche;

import com.agriconnect.agriconnect.entity.marche.ProduitVue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitVueRepository extends JpaRepository<ProduitVue, Long> {
    boolean existsByProduitIdAndUtilisateurId(Long produitId, Long utilisateurId);
}