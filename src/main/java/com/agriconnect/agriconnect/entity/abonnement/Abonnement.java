package com.agriconnect.agriconnect.entity.abonnement;


import com.agriconnect.agriconnect.entity.metier.Utilisateur;
import com.agriconnect.agriconnect.entity.parametre.BaseEntity;
import com.agriconnect.agriconnect.enums.StatutAbonnement;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

// ===== Abonnement.java =====
@Entity
@Table(name = "abonnements", indexes = @Index(columnList = "utilisateur_id, statut"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class Abonnement extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_id")
    private PlanAbonnement plan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private StatutAbonnement statut;

    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;      // null pour le plan gratuit
    private Boolean renouvellementAuto;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paiement_id")
    private Paiement paiement;
}