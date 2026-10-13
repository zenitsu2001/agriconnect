package com.agriconnect.agriconnect.entity.stock;

import com.agriconnect.agriconnect.entity.metier.Utilisateur;
import com.agriconnect.agriconnect.entity.parametre.BaseEntity;
import com.agriconnect.agriconnect.entity.parametre.Region;
import com.agriconnect.agriconnect.enums.StatutStock;
import com.agriconnect.agriconnect.enums.TypeStock;
import com.agriconnect.agriconnect.enums.Unite;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

// ===== ArticleStock.java =====
// Propriétaire = un agriculteur (récoltes, intrants) OU un agent de l'État (semences par région).
@Entity
@Table(name = "articles_stock", indexes = @Index(columnList = "proprietaire_id, type"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class ArticleStock extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proprietaire_id")
    private Utilisateur proprietaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;              // stocks régionaux de l'État (null pour un agriculteur)

    @Column(nullable = false, length = 150)
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TypeStock type;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal quantite;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Unite unite;

    @Column(precision = 12, scale = 2)
    private BigDecimal seuilBas;        // en dessous : statut BAS

    @Column(precision = 12, scale = 2)
    private BigDecimal seuilHaut;       // au-dessus : statut BON

    /** Statut calculé (non persisté). La « dernière mise à jour » est dateModification. */
    @Transient
    public StatutStock getStatut() {
        if (seuilBas != null && quantite.compareTo(seuilBas) <= 0) return StatutStock.BAS;
        if (seuilHaut != null && quantite.compareTo(seuilHaut) >= 0) return StatutStock.BON;
        return StatutStock.NORMAL;
    }
}