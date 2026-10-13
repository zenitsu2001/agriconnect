package com.agriconnect.agriconnect.entity.calendrier;

import com.agriconnect.agriconnect.entity.metier.Agriculteur;
import com.agriconnect.agriconnect.entity.parametre.BaseEntity;
import com.agriconnect.agriconnect.enums.Priorite;
import com.agriconnect.agriconnect.enums.TypeEvenement;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

// ===== Evenement.java =====  (calendrier agricole ; les pluies prévues sont calculées, pas stockées)
@Entity
@Table(name = "evenements", indexes = @Index(columnList = "proprietaire_id, dateEvenement"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class Evenement extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proprietaire_id")
    private Agriculteur proprietaire;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private TypeEvenement type;

    @Column(nullable = false, length = 150)
    private String titre;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private LocalDate dateEvenement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Priorite priorite;

    @Column(nullable = false)
    private Boolean termine;

    @PrePersist
    protected void initialiserDefauts() {
        if (termine == null) termine = false;
        if (priorite == null) priorite = Priorite.MOYENNE;
    }
}