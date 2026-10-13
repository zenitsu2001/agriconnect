package com.agriconnect.agriconnect.repository.marche;


import com.agriconnect.agriconnect.entity.marche.Produit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

    List<Produit> findTop5ByAgriculteurIdOrderByDateCreationDesc(Long agriculteurId);   // « Produits récents »
    List<Produit> findByAgriculteurIdOrderByDateCreationDesc(Long agriculteurId);

    /**
     * Recherche du marché : produits en vente, filtre texte (passer "" si vide) et région (null = toutes),
     * produits boostés en tête. Tri secondaire via le Pageable (prix, vues...).
     */
    @Query(value = """
            select p from Produit p
            where p.statut = com.agriconnect.agriconnect.enums.StatutProduit.EN_VENTE
              and lower(p.nom) like lower(concat('%', :q, '%'))
              and (:regionId is null or p.agriculteur.exploitation.region.id = :regionId)
            order by case when exists (
                select 1 from Boost b
                where b.produit = p and b.dateDebut <= :maintenant and b.dateFin >= :maintenant
            ) then 0 else 1 end
            """,
            countQuery = """
            select count(p) from Produit p
            where p.statut = com.agriconnect.agriconnect.enums.StatutProduit.EN_VENTE
              and lower(p.nom) like lower(concat('%', :q, '%'))
              and (:regionId is null or p.agriculteur.exploitation.region.id = :regionId)
            """)
    Page<Produit> rechercher(@Param("q") String q,
                             @Param("regionId") Long regionId,
                             @Param("maintenant") LocalDateTime maintenant,
                             Pageable pageable);

    @Modifying
    @Query("update Produit p set p.nombreVues = p.nombreVues + 1 where p.id = :id")
    int incrementerVues(@Param("id") Long id);
}
