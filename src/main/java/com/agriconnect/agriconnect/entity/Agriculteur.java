package com.agriconnect.agriconnect.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agriculteurs")
@DiscriminatorValue("AGRICULTEUR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Agriculteur extends Utilisateur {

    private Double superficie;

    @Column(name = "type_exploitation")
    private String typeExploitation;

    private String localisation;

    // ── Produits publiés par l'agriculteur ────────────────────────────────────
    @OneToMany(mappedBy = "agriculteur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Produit> produits = new ArrayList<>();

    // ── Stock géré par l'agriculteur ─────────────────────────────────────────
    @OneToMany(mappedBy = "agriculteur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Stock> stocks = new ArrayList<>();
}