package com.agriconnect.agriconnect.entity.marche;

import com.agriconnect.agriconnect.entity.abonnement.Paiement;
import com.agriconnect.agriconnect.entity.parametre.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// ===== Boost.java =====  (mise en avant payante : 7 jours)
@Entity
@Table(name = "boosts", indexes = @Index(columnList = "produit_id, dateFin"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class Boost extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @Column(nullable = false)
    private LocalDateTime dateDebut;

    @Column(nullable = false)
    private LocalDateTime dateFin;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal montant;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paiement_id")
    private Paiement paiement;

    // Boost.java : AJOUTER ce champ (un boost non payé ne doit jamais compter comme actif)
    @Column(nullable = false)
    private Boolean actif;                 // true seulement après confirmation du paiement (ou si le boost est gratuit)

    @PrePersist
    protected void initialiserDefauts() {
        if (actif == null) actif = false;
    }
    public boolean isEnCours(LocalDateTime maintenant) {
        return !maintenant.isBefore(dateDebut) && !maintenant.isAfter(dateFin);
    }
}
