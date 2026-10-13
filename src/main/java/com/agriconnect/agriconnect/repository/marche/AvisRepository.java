package com.agriconnect.agriconnect.repository.marche;

import com.agriconnect.agriconnect.entity.marche.Avis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AvisRepository extends JpaRepository<Avis, Long> {
    List<Avis> findByProduitIdOrderByDateCreationDesc(Long produitId);
    boolean existsByProduitIdAndAuteurId(Long produitId, Long auteurId);

    @Query("select avg(a.note) from Avis a where a.produit.id = :produitId")
    Double moyenneParProduit(@Param("produitId") Long produitId);
}