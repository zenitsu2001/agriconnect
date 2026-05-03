package com.agriconnect.agriconnect.dto;

import com.agriconnect.agriconnect.enums.TypeAbonnement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO renvoyé après création ou consultation d'un Admin.
 * IMPORTANT : on n'inclut PAS le motDePasse — jamais dans une réponse HTTP.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponse {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private LocalDate dateInscription;
    private TypeAbonnement typeAbonnement;

    // Champs spécifiques Admin
    private String role;
    private Integer niveauAcces;
}