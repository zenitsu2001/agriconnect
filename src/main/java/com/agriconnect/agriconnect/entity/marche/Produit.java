package com.agriconnect.agriconnect.entity.marche;

import com.agriconnect.agriconnect.entity.metier.Agriculteur;
import com.agriconnect.agriconnect.entity.parametre.BaseEntity;
import com.agriconnect.agriconnect.enums.CategorieProduit;
import com.agriconnect.agriconnect.enums.StatutProduit;
import com.agriconnect.agriconnect.enums.Unite;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// ===== Produit.java =====  (annonce publiée sur le marché)
@Entity
@Table(name = "produits", indexes = {
        @Index(columnList = "agriculteur_id"),
        @Index(columnList = "statut")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class Produit extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "agriculteur_id")
    private Agriculteur agriculteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_reference_id")
    private ProduitReference produitReference;

    @Column(nullable = false, length = 150)
    private String nom;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private CategorieProduit categorie;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal quantite;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Unite unite;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal prix;            // prix par unité, en FCFA

    @Column(length = 255)
    private String imageUrl;

    @Column(length = 16)
    private String emoji;               // visuel de repli (nécessite utf8mb4)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private StatutProduit statut;

    @Column(nullable = false)
    private Integer nombreVues;

    @ElementCollection
    @CollectionTable(name = "produit_images", joinColumns = @JoinColumn(name = "produit_id"))
    @OrderColumn(name = "position")              // la première photo est l'image principale
    @Column(name = "url", length = 255)
    @Size(max = 3)                               // jakarta.validation.constraints.Size
    @Builder.Default
    private List<String> images = new ArrayList<>();

    @PrePersist
    protected void initialiserDefauts() {
        if (statut == null) statut = StatutProduit.EN_VENTE;
        if (nombreVues == null) nombreVues = 0;
    }
}
