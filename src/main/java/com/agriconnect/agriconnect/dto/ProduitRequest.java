package com.agriconnect.agriconnect.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO reçu lors de la création ou modification d'un Produit par un Agriculteur.
 * L'agriculteur ne choisit pas lui-même la date de publication : le serveur la génère automatiquement.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProduitRequest {

    @NotBlank(message = "Le nom du produit est obligatoire")
    @Size(max = 200, message = "Le nom ne doit pas dépasser 200 caractères")
    private String nom;

    private String description;

    @NotBlank(message = "La catégorie est obligatoire")
    private String categorie;

    /**
     * @DecimalMin : on ne peut pas mettre en vente un produit avec 0 unité disponible.
     */
    @NotNull(message = "La quantité disponible est obligatoire")
    @DecimalMin(value = "0.01", message = "La quantité doit être supérieure à 0")
    private Double quantiteDisponible;

    @NotBlank(message = "L'unité est obligatoire (ex: kg, litre, sac)")
    private String unite;

    @NotNull(message = "Le prix unitaire est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être strictement positif")
    private Double prixUnitaire;

    /**
     * L'ID de l'agriculteur propriétaire du produit.
     * On passe l'ID et non l'objet entier pour rester léger.
     */
    @NotNull(message = "L'identifiant de l'agriculteur est obligatoire")
    private Long agriculteurId;
}