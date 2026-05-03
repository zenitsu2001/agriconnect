package com.agriconnect.agriconnect.dto;

import com.agriconnect.agriconnect.enums.TypeAbonnement;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO reçu lors de l'inscription d'un Partenaire.
 * Un partenaire peut être une ONG, une banque, un fournisseur d'intrants, etc.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartenaireRequest {

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

    // ── Champs spécifiques à Partenaire ──────────────────────────────────────

    /**
     * Ex : "ONG", "BANQUE", "FOURNISSEUR", "ASSUREUR"
     */
    @NotBlank(message = "Le type de partenariat est obligatoire")
    private String typePartenariat;

    @NotBlank(message = "Le domaine d'expertise est obligatoire")
    private String domaineExpertise;
}