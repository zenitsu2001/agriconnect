package com.agriconnect.agriconnect.entity.messagerie;
import com.agriconnect.agriconnect.entity.metier.Utilisateur;
import com.agriconnect.agriconnect.entity.parametre.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

// ===== Message.java =====  (texte, vocal ou pièce jointe)
@Entity
@Table(name = "messages", indexes = @Index(columnList = "conversation_id, dateEnvoi"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class Message extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "expediteur_id")
    private Utilisateur expediteur;

    @Column(columnDefinition = "TEXT")
    private String contenu;                 // null pour un message vocal

    @Column(length = 255) private String audioUrl;
    private Integer dureeAudioSecondes;

    @Column(length = 255) private String pieceJointeUrl;
    @Column(length = 150) private String pieceJointeNom;

    @Column(nullable = false)
    private LocalDateTime dateEnvoi;

    @Column(nullable = false)
    private Boolean lu;

    @PrePersist
    protected void initialiserDefauts() {
        if (lu == null) lu = false;
        if (dateEnvoi == null) dateEnvoi = LocalDateTime.now();
    }
}
