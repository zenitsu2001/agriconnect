package com.agriconnect.agriconnect.entity.calendrier;

import com.agriconnect.agriconnect.entity.marche.ProduitReference;
import com.agriconnect.agriconnect.entity.metier.Agriculteur;
import com.agriconnect.agriconnect.entity.parametre.BaseEntity;
import com.agriconnect.agriconnect.enums.Unite;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
// ===== CalculPrevente.java =====  (« Enregistrer le calcul » : entrées et résultats)
@Entity
@Table(name = "calculs_preventes", indexes = @Index(columnList = "agriculteur_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class CalculPrevente extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "agriculteur_id")
    private Agriculteur agriculteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_reference_id")
    private ProduitReference produitReference;

    // ----- Saisie -----
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal quantite;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Unite unite;

    @Column(nullable = false, precision = 15, scale = 2) private BigDecimal prixUnitaire;
    @Column(precision = 15, scale = 2) private BigDecimal coutProduction;
    @Column(precision = 15, scale = 2) private BigDecimal fraisTransport;
    @Column(precision = 15, scale = 2) private BigDecimal autresFrais;
    @Column(precision = 5, scale = 2) private BigDecimal tauxRemise;
    @Column(precision = 5, scale = 2) private BigDecimal tauxTaxe;

    // ----- Résultats -----
    @Column(precision = 18, scale = 2) private BigDecimal montantBrut;
    @Column(precision = 18, scale = 2) private BigDecimal montantRemise;
    @Column(precision = 18, scale = 2) private BigDecimal montantTaxe;
    @Column(precision = 18, scale = 2) private BigDecimal coutTotal;
    @Column(precision = 18, scale = 2) private BigDecimal montantFinal;
    @Column(precision = 18, scale = 2) private BigDecimal beneficeNet;
    @Column(precision = 8, scale = 2) private BigDecimal margeBeneficiaire;   // null si aucun coût saisi
    private Boolean prixDansBareme;                                           // null si aucun produit choisi
}