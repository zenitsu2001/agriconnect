package com.agriconnect.agriconnect.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "investisseurs")
@DiscriminatorValue("INVESTISSEUR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Investisseur extends Utilisateur {

    private String entreprise;

    @Column(name = "secteur_activite")
    private String secteurActivite;

    // ── Offres publiées / reçues ──────────────────────────────────────────────
    @OneToMany(mappedBy = "investisseur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Offre> offres = new ArrayList<>();

    // ── Messages envoyés ou reçus ─────────────────────────────────────────────
    @OneToMany(mappedBy = "expediteur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Message> messagesEnvoyes = new ArrayList<>();

    @OneToMany(mappedBy = "destinataire", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Message> messagesRecus = new ArrayList<>();
}