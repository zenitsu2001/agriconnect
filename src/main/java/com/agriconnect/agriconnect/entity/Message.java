package com.agriconnect.agriconnect.entity;

import com.agriconnect.agriconnect.enums.TypeMessage;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenu;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_message")
    private TypeMessage typeMessage;

    @Column(name = "date_envoi")
    private LocalDateTime dateEnvoi;

    @Builder.Default
    private Boolean lu = false;

    @Column(name = "fichier_audio")
    private String fichierAudio;

    // ── Expéditeur (Investisseur) ──────────────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expediteur_id", nullable = false)
    private Investisseur expediteur;

    // ── Destinataire (Investisseur) ───────────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinataire_id", nullable = false)
    private Investisseur destinataire;
}