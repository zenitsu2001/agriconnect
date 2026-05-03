package com.agriconnect.agriconnect.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "offres")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Offre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "type_offre")
    private String typeOffre;

    @Column(name = "date_publication")
    private LocalDate datePublication;

    @Column(name = "date_expiration")
    private LocalDate dateExpiration;

    private Double budget;

    @Builder.Default
    private Boolean booste = false;

    // ── Investisseur ayant publié l'offre ─────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investisseur_id")
    private Investisseur investisseur;

    // ── Partenaire ayant publié l'offre ───────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partenaire_id")
    private Partenaire partenaire;
}