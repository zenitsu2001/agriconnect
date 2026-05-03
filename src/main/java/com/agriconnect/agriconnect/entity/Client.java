package com.agriconnect.agriconnect.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
@DiscriminatorValue("CLIENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Client extends Utilisateur {

    @Column(name = "type_client")
    private String typeClient;

    @Column(name = "raison_sociale")
    private String raisonSociale;

    // ── Commandes passées par le client ───────────────────────────────────────
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Commande> commandes = new ArrayList<>();
}