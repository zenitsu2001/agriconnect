package com.agriconnect.agriconnect.dto;

import com.agriconnect.agriconnect.enums.TypeAbonnement;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO reçu lors de l'inscription d'un Agriculteur.
 * Un agriculteur a les champs communs de Utilisateur + ses informations d'exploitation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgriculteurRequest {

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

    @Pattern(
            regexp = "^\\+?[0-9]{8,15}$",
            message = "Numéro de téléphone invalide"
    )
    private String telephone;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String motDePasse;

    private String adresse;

    private TypeAbonnement typeAbonnement;

    // ── Champs spécifiques à Agriculteur ─────────────────────────────────────

    /**
     * @DecimalMin : la superficie doit être au moins de 0.1 hectare.
     * On utilise DecimalMin (et non Min) car la valeur est un Double (décimal).
     */
    @DecimalMin(value = "0.1", message = "La superficie doit être d'au moins 0.1 hectare")
    private Double superficie;

    @NotBlank(message = "Le type d'exploitation est obligatoire")
    private String typeExploitation;

    @NotBlank(message = "La localisation est obligatoire")
    private String localisation;
}