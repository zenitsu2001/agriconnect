package com.agriconnect.agriconnect.entity.stock;
import com.agriconnect.agriconnect.entity.metier.Utilisateur;
import com.agriconnect.agriconnect.entity.parametre.BaseEntity;
import com.agriconnect.agriconnect.entity.parametre.Region;
import com.agriconnect.agriconnect.enums.TypeMouvement;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// ===== MouvementStock.java =====  (entrées / sorties : alimente « Entrées aujourd'hui » et les statistiques)
@Entity
@Table(name = "mouvements_stock", indexes = @Index(columnList = "dateMouvement"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class MouvementStock extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "article_id")
    private ArticleStock article;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TypeMouvement type;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal quantite;

    @Column(nullable = false)
    private LocalDateTime dateMouvement;

    @Column(length = 255)
    private String motif;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinataire_id")
    private Utilisateur destinataire;   // bénéficiaire d'une distribution (sortie)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;              // région de distribution (statistiques de l'État)
}