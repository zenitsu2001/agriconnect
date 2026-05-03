package com.agriconnect.agriconnect.dto;

import com.agriconnect.agriconnect.enums.TypeAbonnement;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO reçu lors de la création d'un compte Admin.
 * Un Admin est un Utilisateur avec des champs supplémentaires (role, niveauAcces).
 * On regroupe ici TOUS les champs nécessaires : ceux hérités de Utilisateur + ceux propres à Admin.
 *
 * Pourquoi ne pas hériter d'un UtilisateurRequest ?
 * → Les DTOs sont volontairement plats (pas d'héritage) pour rester simples
 *   et éviter des problèmes de sérialisation JSON avec Jackson.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminRequest {

    // ── Champs hérités de Utilisateur ────────────────────────────────────────

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 100, message = "Le prénom doit contenir entre 2 et 100 caractères")
    private String prenom;

    /**
     * @Email : vérifie que la chaîne respecte le format "xxx@xxx.xx".
     * @NotBlank : refuse les chaînes vides ou composées uniquement d'espaces.
     */
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @Pattern(
            regexp = "^\\+?[0-9]{8,15}$",
            message = "Numéro de téléphone invalide (8 à 15 chiffres, + optionnel en début)"
    )
    private String telephone;

    /**
     * @Size(min=8) : impose un minimum de sécurité sur le mot de passe.
     * En pratique, le service devra hasher ce mot de passe avant de le stocker (BCrypt).
     */
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String motDePasse;

    private String adresse;

    private TypeAbonnement typeAbonnement;

    // ── Champs spécifiques à Admin ───────────────────────────────────────────

    @NotBlank(message = "Le rôle est obligatoire")
    private String role;

    /**
     * @Min(1) @Max(5) : le niveau d'accès est un entier borné entre 1 (lecture) et 5 (super-admin).
     */
    @NotNull(message = "Le niveau d'accès est obligatoire")
    @Min(value = 1, message = "Le niveau d'accès minimum est 1")
    @Max(value = 5, message = "Le niveau d'accès maximum est 5")
    private Integer niveauAcces;
}