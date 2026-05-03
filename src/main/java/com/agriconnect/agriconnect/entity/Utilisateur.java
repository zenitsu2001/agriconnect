package com.agriconnect.agriconnect.entity;

import com.agriconnect.agriconnect.enums.TypeAbonnement;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "utilisateur")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type_utilisateur", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String email;

    private String telephone;

    @Column(nullable = false)
    private String motDePasse;

    private String adresse;

    @Column(name = "date_inscription")
    private LocalDate dateInscription;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_abonnement")
    private TypeAbonnement typeAbonnement;

    // ── Relation avec Abonnement ──────────────────────────────────────────────
    @OneToOne(mappedBy = "utilisateur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Abonnement abonnement;
}