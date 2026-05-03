package com.agriconnect.agriconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO renvoyé lors de la consultation d'un mouvement de stock.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockResponse {

    private Long id;
    private String typeStock;
    private Double quantite;
    private LocalDate dateEntree;
    private LocalDate dateSortie;
    private String destination;
    private String origine;

    // Résumé de l'agriculteur propriétaire
    private Long agriculteurId;
    private String agriculteurNomComplet;
}