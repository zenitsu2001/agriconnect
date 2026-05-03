package com.agriconnect.agriconnect.dto;

import com.agriconnect.agriconnect.enums.TypeAbonnement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO renvoyé lors de la consultation d'un compte Etat.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EtatResponse {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private LocalDate dateInscription;
    private TypeAbonnement typeAbonnement;

    // Champs spécifiques Etat
    private String region;
    private String departement;
}