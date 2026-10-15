package com.agriconnect.agriconnect.entity.metier;


import com.agriconnect.agriconnect.entity.parametre.BaseEntity;
import com.agriconnect.agriconnect.entity.parametre.LangueSupportee;
import com.agriconnect.agriconnect.enums.Langue;
import com.agriconnect.agriconnect.enums.Profil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

// ===== Utilisateur.java =====
@Entity
@Table(name = "utilisateurs", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public abstract class Utilisateur extends BaseEntity {

    @Column(nullable = false, length = 80)
    private String nom;

    @Column(length = 80)
    private String prenom;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false, length = 30)
    private String telephone;

    @Column(length = 255)
    private String adresse;

    @Column(nullable = false, length = 255)
    private String motDePasse;      // toujours le HASH (BCrypt), jamais le mot de passe en clair

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false, length = 5)
//    private Langue langue;

    @Column(nullable = false)
    private Boolean actif;

    @Column(length = 255)
    private String photoProfilUrl;

    private LocalDateTime derniereConnexion;

    /** Profil déduit de la sous-classe (non persisté) : sert aux rôles JWT et à la navigation. */
    @Transient
    public abstract Profil getProfil();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "langue")
    private LangueSupportee langue;

//    @PrePersist
//    protected void initialiserDefauts() {
//        if (actif == null) actif = true;
//        if (langue == null) langue = Langue.FR;
//    }
}