package com.agriconnect.agriconnect.dto;

import com.agriconnect.agriconnect.enums.TypeAbonnement;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO reçu quand un utilisateur souscrit ou renouvelle un abonnement.
 * On n'expose pas l'ID ici : c'est le serveur qui le génère, jamais le client.
 */
@Data               // Génère getters, setters, equals, hashCode, toString (Lombok)
@Builder            // Permet la construction fluide : AbonnementRequest.builder().typeAbonnement(...).build()
@NoArgsConstructor  // Constructeur vide requis par Jackson pour désérialiser le JSON entrant
@AllArgsConstructor // Constructeur avec tous les champs, utilisé par @Builder
public class AbonnementRequest {

    /**
     * @NotNull : le type est obligatoire, on ne peut pas souscrire sans choisir une formule.
     * TypeAbonnement est un enum : le client envoie "GRATUIT", "PREMIUM_MENSUEL" ou "PREMIUM_ANNUEL".
     */
    @NotNull(message = "Le type d'abonnement est obligatoire")
    private TypeAbonnement typeAbonnement;

    /**
     * @NotNull : la date de début est obligatoire.
     * LocalDate est sérialisé/désérialisé au format ISO : "2025-01-15"
     */
    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;

    /**
     * La date de fin peut être nulle pour les abonnements sans limite (ex: GRATUIT).
     */
    private LocalDate dateFin;

    /**
     * @Positive : le montant doit être strictement supérieur à 0 s'il est renseigné.
     */
    @Positive(message = "Le montant doit être positif")
    private Double montant;

    /**
     * ID de l'utilisateur concerné.
     * On ne met pas l'objet Utilisateur entier pour éviter une dépendance circulaire dans le JSON.
     */
    @NotNull(message = "L'identifiant de l'utilisateur est obligatoire")
    private Long utilisateurId;
}