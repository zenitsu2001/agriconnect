package com.agriconnect.agriconnect.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String description;

    private String categorie;

    @Column(name = "quantite_disponible")
    private Double quantiteDisponible;

    private String unite;

    @Column(name = "prix_unitaire")
    private Double prixUnitaire;

    @Column(name = "date_publication")
    private LocalDate datePublication;

    @Builder.Default
    private Boolean booste = false;

    // ── Agriculteur propriétaire ──────────────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agriculteur_id", nullable = false)
    private Agriculteur agriculteur;

    // ── Lignes de commande contenant ce produit ───────────────────────────────
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Commande> commandes = new ArrayList<>();
}