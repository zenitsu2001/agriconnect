package com.agriconnect.agriconnect.entity;

import com.agriconnect.agriconnect.enums.StatutCommande;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "commandes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_commande")
    private LocalDate dateCommande;

    private Double quantite;

    @Column(name = "prix_total")
    private Double prixTotal;

    @Enumerated(EnumType.STRING)
    private StatutCommande statut;

    @Column(name = "mode_paiement")
    private String modePaiement;

    @Column(name = "adresse_livraison")
    private String adresseLivraison;

    // ── Client ayant passé la commande ────────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    // ── Produit commandé ──────────────────────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;
}