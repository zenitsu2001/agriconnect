package com.agriconnect.agriconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO renvoyé lors de la consultation d'une Offre.
 * On inclut un résumé du publieur (investisseur OU partenaire selon le cas).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OffreResponse {

    private Long id;
    private String titre;
    private String description;
    private String typeOffre;
    private LocalDate datePublication;
    private LocalDate dateExpiration;
    private Double budget;

    /**
     * true = offre mise en avant dans les résultats de recherche.
     */
    private Boolean booste;

    /**
     * Ces deux champs sont mutuellement exclusifs :
     * - si l'offre vient d'un investisseur → investisseurId renseigné, partenaireId = null
     * - si l'offre vient d'un partenaire   → partenaireId renseigné, investisseurId = null
     */
    private Long investisseurId;
    private String investisseurNomComplet;

    private Long partenaireId;
    private String partenaireNomComplet;
}