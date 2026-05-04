package com.agriconnect.agriconnect.entity;

import com.agriconnect.agriconnect.entity.Produit;
import com.agriconnect.agriconnect.entity.Stock;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agriculteurs")
@DiscriminatorValue("AGRICULTEUR")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Agriculteur extends Utilisateur {

    private Double superficie;

    @Column(name = "type_exploitation")
    private String typeExploitation;

    private String localisation;

    /**
     * @Builder.Default est incompatible avec @SuperBuilder.
     * Avec @SuperBuilder, on initialise les listes directement ici.
     * Le constructeur sans argument de @NoArgsConstructor les initialisera aussi.
     */
    @OneToMany(mappedBy = "agriculteur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Produit> produits = new ArrayList<>();

    @OneToMany(mappedBy = "agriculteur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Stock> stocks = new ArrayList<>();
}