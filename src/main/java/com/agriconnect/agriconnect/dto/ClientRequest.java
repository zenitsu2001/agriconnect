package com.agriconnect.agriconnect.dto;

import com.agriconnect.agriconnect.enums.TypeAbonnement;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO reçu lors de l'inscription d'un Client.
 * Un client peut être un particulier ou une entreprise (raisonSociale).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientRequest {

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

    // ── Champs spécifiques à Client ──────────────────────────────────────────

    /**
     * Ex: "PARTICULIER", "ENTREPRISE", "RESTAURATEUR", etc.
     */
    @NotBlank(message = "Le type de client est obligatoire")
    private String typeClient;

    /**
     * Optionnel : renseigné uniquement si le client est une entreprise.
     */
    private String raisonSociale;
}