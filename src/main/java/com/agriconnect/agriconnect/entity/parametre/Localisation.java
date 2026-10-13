package com.agriconnect.agriconnect.entity.parametre;// ===== Region.java =====  (14 régions)
import jakarta.persistence.*;
import lombok.*;


// ===== Localisation.java =====  (valeur intégrée : exploitation de l'agriculteur)
@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Localisation {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departement_id")
    private Departement departement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commune_id")
    private Commune commune;

    private Double latitude;       // coordonnées utilisées pour la météo
    private Double longitude;

    @Column(length = 150)
    private String libelle;        // texte affiché (commune, département ou région)
}