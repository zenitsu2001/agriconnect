package com.agriconnect.agriconnect.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO reçu lors d'un mouvement de stock (entrée ou sortie de marchandise).
 * Un "mouvement" correspond à une ligne de stock enregistrée par l'agriculteur.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockRequest {

    @NotBlank(message = "Le type de stock est obligatoire (ex: SEMENCES, ENGRAIS, RÉCOLTE)")
    private String typeStock;

    @NotNull(message = "La quantité est obligatoire")
    @DecimalMin(value = "0.01", message = "La quantité doit être supérieure à 0")
    private Double quantite;

    @NotNull(message = "La date d'entrée est obligatoire")
    private LocalDate dateEntree;

    /**
     * Date de sortie optionnelle : renseignée quand la marchandise quitte le stock.
     */
    private LocalDate dateSortie;

    /**
     * Destination du stock sorti : nom d'un client, d'un marché, etc.
     */
    private String destination;

    /**
     * Origine du stock entré : nom d'un fournisseur, production propre, etc.
     */
    private String origine;

    @NotNull(message = "L'identifiant de l'agriculteur est obligatoire")
    private Long agriculteurId;
}