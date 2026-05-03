package com.agriconnect.agriconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO renvoyé lors de la consultation d'un Produit.
 * On inclut des infos résumées sur l'agriculteur pour que le client
 * sache qui vend ce produit, sans charger tout son profil.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProduitResponse {

    private Long id;
    private String nom;
    private String description;
    private String categorie;
    private Double quantiteDisponible;
    private String unite;
    private Double prixUnitaire;
    private LocalDate datePublication;

    /**
     * Indique si le produit est mis en avant (boosted).
     * Les produits boostés apparaissent en tête de liste.
     */
    private Boolean booste;

    /**
     * Infos résumées de l'agriculteur vendeur.
     * On évite d'imbriquer tout le AgriculteurResponse pour ne pas alourdir la réponse.
     */
    private Long agriculteurId;
    private String agriculteurNomComplet;
    private String agriculteurLocalisation;
}