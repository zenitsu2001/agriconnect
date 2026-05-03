package com.agriconnect.agriconnect.dto;

import com.agriconnect.agriconnect.enums.TypeAbonnement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO renvoyé au client après création ou consultation d'un abonnement.
 * Pas de validation ici : c'est une réponse, on ne valide pas ce qu'on envoie.
 * On inclut l'ID car le client en a besoin pour référencer la ressource.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AbonnementResponse {

    private Long id;
    private TypeAbonnement typeAbonnement;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Double montant;

    /**
     * On renvoie le statut sous forme de booléen lisible.
     * "true" = abonnement actif, "false" = inactif/résilié.
     */
    private Boolean statut;

    /**
     * On ne renvoie pas l'objet Utilisateur complet (évite la récursion infinie JSON).
     * On renvoie uniquement son ID et son nom complet pour que le client sache à qui appartient l'abonnement.
     */
    private Long utilisateurId;
    private String utilisateurNomComplet;
}