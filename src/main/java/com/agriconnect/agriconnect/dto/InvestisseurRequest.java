package com.agriconnect.agriconnect.dto;

import com.agriconnect.agriconnect.enums.TypeAbonnement;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO reçu lors de l'inscription d'un Investisseur.
 * Un investisseur représente une entreprise ou un individu qui finance des projets agricoles.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestisseurRequest {

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

    // ── Champs spécifiques à Investisseur ────────────────────────────────────

    @NotBlank(message = "Le nom de l'entreprise est obligatoire")
    private String entreprise;

    @NotBlank(message = "Le secteur d'activité est obligatoire")
    private String secteurActivite;
}