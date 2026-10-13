package com.agriconnect.agriconnect.repository.stock;

import com.agriconnect.agriconnect.entity.stock.MouvementStock;
import com.agriconnect.agriconnect.enums.TypeMouvement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface MouvementStockRepository extends JpaRepository<MouvementStock, Long> {

    /** « Entrées aujourd'hui ». */
    long countByArticleProprietaireIdAndTypeAndDateMouvementGreaterThanEqual(
            Long proprietaireId, TypeMouvement type, LocalDateTime depuis);

    interface TotalMensuel { Integer getAnnee(); Integer getMois(); TypeMouvement getType(); BigDecimal getTotal(); }

    /** Évolution mensuelle (entrées / sorties) pour les statistiques nationales. */
    @Query("""
            select year(m.dateMouvement) as annee, month(m.dateMouvement) as mois,
                   m.type as type, sum(m.quantite) as total
            from MouvementStock m
            where m.dateMouvement >= :depuis
            group by year(m.dateMouvement), month(m.dateMouvement), m.type
            order by year(m.dateMouvement), month(m.dateMouvement)
            """)
    List<TotalMensuel> totauxMensuels(@Param("depuis") LocalDateTime depuis);

    interface TotalParRegion { Long getRegionId(); BigDecimal getTotal(); }

    /** Quantités distribuées par région sur une période. */
    @Query("""
            select m.region.id as regionId, sum(m.quantite) as total
            from MouvementStock m
            where m.type = com.agriconnect.agriconnect.enums.TypeMouvement.SORTIE
              and m.region is not null and m.dateMouvement >= :depuis
            group by m.region.id
            """)
    List<TotalParRegion> distributionParRegion(@Param("depuis") LocalDateTime depuis);
}

