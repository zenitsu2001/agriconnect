package com.agriconnect.agriconnect.entity.messagerie;
import com.agriconnect.agriconnect.entity.metier.Utilisateur;
import com.agriconnect.agriconnect.entity.parametre.BaseEntity;
import com.agriconnect.agriconnect.enums.TypeNotification;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
// ===== Notification.java =====  (cloche de la barre supérieure)
@Entity
@Table(name = "notifications", indexes = @Index(columnList = "destinataire_id, lue"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destinataire_id")
    private Utilisateur destinataire;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TypeNotification type;

    @Column(nullable = false, length = 200)
    private String titre;

    @Column(length = 500)
    private String message;

    @Column(length = 255)
    private String lien;                // route Angular, ex. /agriculteur/calendar

    @Column(nullable = false)
    private Boolean lue;

    @PrePersist
    protected void initialiserDefauts() {
        if (lue == null) lue = false;
    }
}
