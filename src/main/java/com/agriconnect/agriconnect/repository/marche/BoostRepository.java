package com.agriconnect.agriconnect.repository.marche;

import com.agriconnect.agriconnect.entity.marche.Boost;
import com.agriconnect.agriconnect.entity.marche.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BoostRepository extends JpaRepository<Boost, Long> {
    boolean existsByProduitIdAndDateFinAfter(Long produitId, LocalDateTime maintenant);
    List<Boost> findByProduitIdOrderByDateFinDesc(Long produitId);
    boolean existsByProduitIdAndActifTrueAndDateFinAfter(Long produitId, LocalDateTime maintenant);
    Optional<Boost> findFirstByProduitIdAndActifTrueAndDateFinAfterOrderByDateFinDesc(Long produitId, LocalDateTime maintenant);
    Optional<Boost> findByPaiementId(Long paiementId);

    @Query("select distinct b.produit.id from Boost b where b.produit.id in :ids and b.actif = true " +
            "and b.dateDebut <= :maintenant and b.dateFin >= :maintenant")
    List<Long> produitsBoostes(@Param("ids") Collection<Long> ids, @Param("maintenant") LocalDateTime maintenant);
}