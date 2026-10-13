package com.agriconnect.agriconnect.entity.messagerie;
import com.agriconnect.agriconnect.entity.metier.Etat;
import com.agriconnect.agriconnect.entity.parametre.BaseEntity;
import com.agriconnect.agriconnect.entity.parametre.Region;
import com.agriconnect.agriconnect.enums.CategoriePublication;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

// ===== Publication.java =====  (normes, actualités, alertes publiées par l'État)
@Entity
@Table(name = "publications")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class Publication extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "auteur_id")
    private Etat auteur;

    @Column(nullable = false, length = 200)
    private String titre;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenu;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private CategoriePublication categorie;

    @Column(nullable = false)
    private Boolean publie;

    private LocalDateTime datePublication;

    @Column(nullable = false)
    private Boolean notifierAgriculteurs;

    /** Régions visées ; liste vide = toutes les régions. */
    @ManyToMany
    @JoinTable(name = "publication_regions",
            joinColumns = @JoinColumn(name = "publication_id"),
            inverseJoinColumns = @JoinColumn(name = "region_id"))
    @Builder.Default
    private Set<Region> regionsCibles = new HashSet<>();

    @PrePersist
    protected void initialiserDefauts() {
        if (publie == null) publie = false;
        if (notifierAgriculteurs == null) notifierAgriculteurs = false;
    }
}