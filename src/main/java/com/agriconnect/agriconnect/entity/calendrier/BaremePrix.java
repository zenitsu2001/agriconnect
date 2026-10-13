package com.agriconnect.agriconnect.entity.calendrier;

import com.agriconnect.agriconnect.entity.marche.ProduitReference;
import com.agriconnect.agriconnect.entity.metier.Etat;
import com.agriconnect.agriconnect.entity.parametre.BaseEntity;
import com.agriconnect.agriconnect.enums.Unite;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
// ===== BaremePrix.java =====  (tarifs de référence fixés par l'État)
@Entity
@Table(name = "baremes_prix",
        uniqueConstraints = @UniqueConstraint(columnNames = {"produit_reference_id", "unite"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class BaremePrix extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produit_reference_id")
    private ProduitReference produitReference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Unite unite;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal prixReference;   // « prix de base » de l'écran d'administration

    @Column(precision = 15, scale = 2)
    private BigDecimal prixMin;         // fourchette utilisée par le calcul des préventes

    @Column(precision = 15, scale = 2)
    private BigDecimal prixMax;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auteur_id")
    private Etat auteur;
}
