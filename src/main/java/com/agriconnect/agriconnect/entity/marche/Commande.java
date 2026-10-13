package com.agriconnect.agriconnect.entity.marche;

import com.agriconnect.agriconnect.entity.metier.Utilisateur;
import com.agriconnect.agriconnect.entity.parametre.BaseEntity;
import com.agriconnect.agriconnect.enums.StatutCommande;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
// ===== Commande.java =====  (bouton « Commander » ; un produit par commande)
@Entity
@Table(name = "commandes", indexes = {
        @Index(columnList = "acheteur_id"),
        @Index(columnList = "produit_id")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class Commande extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "acheteur_id")
    private Utilisateur acheteur;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal quantite;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal prixUnitaire;    // prix figé au moment de la commande

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal montantTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private StatutCommande statut;

    @Column(length = 255)
    private String adresseLivraison;

    private LocalDate dateLivraisonSouhaitee;

    @PrePersist
    protected void initialiserDefauts() {
        if (statut == null) statut = StatutCommande.EN_ATTENTE;
    }
}