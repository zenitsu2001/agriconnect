package com.agriconnect.agriconnect.entity.marche;

import com.agriconnect.agriconnect.entity.metier.Utilisateur;
import com.agriconnect.agriconnect.entity.parametre.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "produit_vues",
        uniqueConstraints = @UniqueConstraint(columnNames = {"produit_id", "utilisateur_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class ProduitVue extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;
}