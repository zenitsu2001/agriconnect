package com.agriconnect.agriconnect.dto;

import com.agriconnect.agriconnect.enums.TypeAbonnement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO renvoyé lors de la consultation d'un profil Partenaire.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartenaireResponse {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private LocalDate dateInscription;
    private TypeAbonnement typeAbonnement;

    // Champs spécifiques Partenaire
    private String typePartenariat;
    private String domaineExpertise;

    /**
     * Nombre d'offres publiées par ce partenaire.
     */
    private Integer nombreOffres;
}