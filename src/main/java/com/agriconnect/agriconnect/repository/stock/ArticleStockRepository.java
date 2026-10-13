package com.agriconnect.agriconnect.repository.stock;


import com.agriconnect.agriconnect.entity.stock.ArticleStock;
import com.agriconnect.agriconnect.enums.TypeStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ArticleStockRepository extends JpaRepository<ArticleStock, Long> {

    List<ArticleStock> findByProprietaireIdOrderByDateModificationDesc(Long proprietaireId);

    @Query("""
            select a from ArticleStock a
            where a.proprietaire.id = :proprietaireId
              and lower(a.nom) like lower(concat('%', :q, '%'))
              and (:type is null or a.type = :type)
            order by a.dateModification desc
            """)
    List<ArticleStock> rechercher(@Param("proprietaireId") Long proprietaireId,
                                  @Param("q") String q,
                                  @Param("type") TypeStock type);

    /** « Stock total » : on n'additionne que les articles en kg (pas de mélange kg / litres). */
    @Query("select coalesce(sum(a.quantite), 0) from ArticleStock a " +
            "where a.proprietaire.id = :proprietaireId and a.unite = com.agriconnect.agriconnect.enums.Unite.KG")
    BigDecimal totalEnKg(@Param("proprietaireId") Long proprietaireId);

    /** « Stock bas » : quantité inférieure ou égale au seuil bas. */
    @Query("select count(a) from ArticleStock a " +
            "where a.proprietaire.id = :proprietaireId and a.seuilBas is not null and a.quantite <= a.seuilBas")
    long compterStockBas(@Param("proprietaireId") Long proprietaireId);
}
