package com.agriconnect.agriconnect.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO reçu lors de la publication d'une Offre par un Investisseur ou un Partenaire.
 *
 * Règle métier importante :
 * Une offre appartient soit à un Investisseur, soit à un Partenaire — jamais aux deux.
 * Cette contrainte sera vérifiée dans la couche Service (pas dans le DTO).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OffreRequest {

    @NotBlank(message = "Le titre de l'offre est obligatoire")
    @Size(max = 255, message = "Le titre ne doit pas dépasser 255 caractères")
    private String titre;

    private String description;

    @NotBlank(message = "Le type d'offre est obligatoire (ex: FINANCEMENT, PARTENARIAT, SERVICE)")
    private String typeOffre;

    /**
     * La date d'expiration est facultative.
     * Si elle est nulle, l'offre reste active indéfiniment jusqu'à suppression manuelle.
     */
    private LocalDate dateExpiration;

    @DecimalMin(value = "0.0", message = "Le budget ne peut pas être négatif")
    private Double budget;

    /**
     * ID de l'investisseur publiant l'offre (null si c'est un partenaire).
     */
    private Long investisseurId;

    /**
     * ID du partenaire publiant l'offre (null si c'est un investisseur).
     */
    private Long partenaireId;
}