package com.agriconnect.agriconnect.entity.parametre;
import jakarta.persistence.*;
import lombok.*;

// LangueSupportee.java : code = identifiant utilisé par Angular (fr, en, wo)
@Entity @Table(name = "langues_supportees")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LangueSupportee {
    @Id @Column(length = 5) private String code;
    @Column(nullable = false, length = 50) private String nom;         // libellé en français
    @Column(nullable = false, length = 50) private String nomNatif;    // affiché dans le sélecteur
    @Column(nullable = false) private Boolean actif;
    @Column(nullable = false) private Boolean parDefaut;
    @Column(nullable = false) private Integer ordre;
}