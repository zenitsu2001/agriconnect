package com.agriconnect.agriconnect.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO reçu quand un Client passe une commande.
 *
 * Note sur les champs absents :
 * - dateCommande : générée automatiquement côté serveur (LocalDate.now())
 * - prixTotal    : calculé par le serveur (quantite × prixUnitaire du produit)
 * - statut       : initialisé à EN_ATTENTE par le serveur
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandeRequest {

    @NotNull(message = "L'identifiant du client est obligatoire")
    private Long clientId;

    @NotNull(message = "L'identifiant du produit est obligatoire")
    private Long produitId;

    @NotNull(message = "La quantité est obligatoire")
    @DecimalMin(value = "0.01", message = "La quantité doit être supérieure à 0")
    private Double quantite;

    @NotBlank(message = "Le mode de paiement est obligatoire")
    private String modePaiement;

    @NotBlank(message = "L'adresse de livraison est obligatoire")
    private String adresseLivraison;
}