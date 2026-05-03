package com.agriconnect.agriconnect.dto;

import com.agriconnect.agriconnect.enums.TypeAbonnement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO renvoyé lors de la consultation d'un profil Investisseur.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestisseurResponse {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private LocalDate dateInscription;
    private TypeAbonnement typeAbonnement;

    // Champs spécifiques Investisseur
    private String entreprise;
    private String secteurActivite;

    /**
     * Nombre d'offres publiées par cet investisseur.
     * On évite de charger la liste complète des offres dans chaque réponse.
     */
    private Integer nombreOffres;
}