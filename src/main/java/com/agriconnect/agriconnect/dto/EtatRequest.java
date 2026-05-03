package com.agriconnect.agriconnect.dto;

import com.agriconnect.agriconnect.enums.TypeAbonnement;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO reçu lors de la création d'un compte Etat (entité gouvernementale).
 * L'Etat est un acteur qui fixe les tarifs et publie des normes sur la plateforme.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EtatRequest {

    // ── Champs hérités de Utilisateur ────────────────────────────────────────

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 100, message = "Le prénom doit contenir entre 2 et 100 caractères")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Numéro de téléphone invalide")
    private String telephone;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String motDePasse;

    private String adresse;
    private TypeAbonnement typeAbonnement;

    // ── Champs spécifiques à Etat ─────────────────────────────────────────────

    @NotBlank(message = "La région est obligatoire")
    private String region;

    @NotBlank(message = "Le département est obligatoire")
    private String departement;
}