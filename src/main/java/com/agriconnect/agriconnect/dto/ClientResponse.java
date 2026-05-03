package com.agriconnect.agriconnect.dto;

import com.agriconnect.agriconnect.enums.TypeAbonnement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO renvoyé lors de la consultation d'un profil Client.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private LocalDate dateInscription;
    private TypeAbonnement typeAbonnement;

    // Champs spécifiques Client
    private String typeClient;
    private String raisonSociale;

    /**
     * Nombre total de commandes passées par ce client.
     * Donnée calculée — évite de charger toute la liste des commandes.
     */
    private Integer nombreCommandes;
}