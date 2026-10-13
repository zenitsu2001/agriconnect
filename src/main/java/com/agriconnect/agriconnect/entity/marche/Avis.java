package com.agriconnect.agriconnect.entity.marche;

import com.agriconnect.agriconnect.entity.metier.Utilisateur;
import com.agriconnect.agriconnect.entity.parametre.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

// ===== Avis.java =====  (note 1 à 5)
@Entity
@Table(name = "avis", uniqueConstraints = @UniqueConstraint(columnNames = {"produit_id", "auteur_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class Avis extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "auteur_id")
    private Utilisateur auteur;

    @Column(nullable = false)
    private Integer note;               // à valider entre 1 et 5 côté service

    @Column(length = 500)
    private String commentaire;
}
