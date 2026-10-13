package com.agriconnect.agriconnect.entity.marche;

import com.agriconnect.agriconnect.entity.parametre.BaseEntity;
import com.agriconnect.agriconnect.enums.CategorieProduit;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

// ===== ProduitReference.java =====  (catalogue : tomate, oignon, pomme de terre...)
@Entity
@Table(name = "produits_reference")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class ProduitReference extends BaseEntity {

    @Column(nullable = false, unique = true, length = 40)
    private String code;                // ex. "tomato" (clé de traduction farmer.presales.products.<code>)

    @Column(nullable = false, length = 80)
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private CategorieProduit categorie;
}
