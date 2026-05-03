package com.agriconnect.agriconnect.dto;

import com.agriconnect.agriconnect.enums.TypeAbonnement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO renvoyé lors de la consultation d'un profil Agriculteur.
 * On inclut un résumé de ses produits sous forme de compteur
 * pour éviter de charger toute la liste (performance).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgriculteurResponse {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private LocalDate dateInscription;
    private TypeAbonnement typeAbonnement;

    // Champs spécifiques Agriculteur
    private Double superficie;
    private String typeExploitation;
    private String localisation;

    /**
     * Nombre de produits publiés par cet agriculteur.
     * C'est une donnée calculée, pas un champ de l'entité.
     * On évite ainsi de sérialiser toute la liste de produits dans chaque réponse.
     */
    private Integer nombreProduits;
}