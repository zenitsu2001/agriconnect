package com.agriconnect.agriconnect.entity.abonnement;

import com.agriconnect.agriconnect.entity.parametre.BaseEntity;
import com.agriconnect.agriconnect.enums.PeriodeAbonnement;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// ===== PlanAbonnement.java =====
@Entity
@Table(name = "plans_abonnement")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class PlanAbonnement extends BaseEntity {

    /** gratuit | mensuel | annuel : sert aussi de clé de traduction (subscription.plans.<code>.name). */
    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal prix;

    @Column(precision = 15, scale = 2)
    private BigDecimal prixOriginal;    // prix barré (offre annuelle)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private PeriodeAbonnement periode;

    private Boolean populaire;
    private Boolean actif;
    private Integer ordreAffichage;

    /** Codes de fonctionnalités incluses (clés subscription.features.*). */
    @ElementCollection
    @CollectionTable(name = "plan_fonctionnalites", joinColumns = @JoinColumn(name = "plan_id"))
    @OrderColumn(name = "position")
    @Column(name = "code", length = 60)
    @Builder.Default
    private List<String> fonctionnalites = new ArrayList<>();

    /** Codes de limitations (clés subscription.limitations.*). */
    @ElementCollection
    @CollectionTable(name = "plan_limitations", joinColumns = @JoinColumn(name = "plan_id"))
    @OrderColumn(name = "position")
    @Column(name = "code", length = 60)
    @Builder.Default
    private List<String> limitations = new ArrayList<>();
}