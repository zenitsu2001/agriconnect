package com.agriconnect.agriconnect.entity.parametre;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
// ===== Region.java =====  (14 régions)
@Entity
@Table(name = "regions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class Region extends BaseEntity {

    @Column(nullable = false, unique = true, length = 40)
    private String code;            // ex. "thies" (identique aux ids du frontend)

    @Column(nullable = false, length = 80)
    private String nom;
}
