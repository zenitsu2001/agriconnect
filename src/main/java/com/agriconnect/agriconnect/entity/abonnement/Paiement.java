package com.agriconnect.agriconnect.entity.abonnement;

import com.agriconnect.agriconnect.entity.metier.Utilisateur;
import com.agriconnect.agriconnect.entity.parametre.BaseEntity;
import com.agriconnect.agriconnect.enums.ModePaiement;
import com.agriconnect.agriconnect.enums.ObjetPaiement;
import com.agriconnect.agriconnect.enums.StatutPaiement;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;


// ===== Paiement.java =====
// On ne stocke QUE la référence du prestataire (Wave, Orange Money, agrégateur) : jamais de numéro de carte.
@Entity
@Table(name = "paiements", indexes = @Index(columnList = "utilisateur_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class Paiement extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal montant;

    @Column(nullable = false, length = 3)
    private String devise;              // XOF par défaut

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private ModePaiement mode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private StatutPaiement statut;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private ObjetPaiement objet;

    @Column(unique = true, length = 100)
    private String referenceExterne;    // identifiant de transaction chez le prestataire

    private LocalDateTime dateConfirmation;

    @PrePersist
    protected void initialiserDefauts() {
        if (devise == null) devise = "XOF";
        if (statut == null) statut = StatutPaiement.EN_ATTENTE;
    }
}