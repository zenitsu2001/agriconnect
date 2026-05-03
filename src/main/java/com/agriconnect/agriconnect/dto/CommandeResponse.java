package com.agriconnect.agriconnect.dto;

import com.agriconnect.agriconnect.enums.StatutCommande;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO renvoyé après création ou consultation d'une Commande.
 * On inclut un résumé du produit et du client pour éviter des appels API supplémentaires.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandeResponse {

    private Long id;
    private LocalDate dateCommande;
    private Double quantite;
    private Double prixTotal;
    private StatutCommande statut;
    private String modePaiement;
    private String adresseLivraison;

    // Résumé du client
    private Long clientId;
    private String clientNomComplet;

    // Résumé du produit commandé
    private Long produitId;
    private String produitNom;
    private String produitUnite;
    private Double produitPrixUnitaire;
}