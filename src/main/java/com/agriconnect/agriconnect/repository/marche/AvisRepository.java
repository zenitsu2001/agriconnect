package com.agriconnect.agriconnect.repository.marche;

import com.agriconnect.agriconnect.entity.marche.Avis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface AvisRepository extends JpaRepository<Avis, Long> {
    List<Avis> findByProduitIdOrderByDateCreationDesc(Long produitId);
    boolean existsByProduitIdAndAuteurId(Long produitId, Long auteurId);

    @Query("select avg(a.note) from Avis a where a.produit.id = :produitId")
    Double moyenneParProduit(@Param("produitId") Long produitId);

    interface StatsAvis { Long getProduitId(); Double getMoyenne(); Long getNombre(); }

    /** Note moyenne et nombre d'avis de PLUSIEURS produits en une seule requête (évite 1 requête par produit). */
    @Query("select a.produit.id as produitId, avg(a.note) as moyenne, count(a) as nombre " +
            "from Avis a where a.produit.id in :ids group by a.produit.id")
    List<StatsAvis> statistiquesParProduits(@Param("ids") Collection<Long> ids);
}