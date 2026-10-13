package com.agriconnect.agriconnect.entity.parametre;// ===== Region.java =====  (14 régions)

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


// ===== Commune.java =====  (à alimenter depuis une source officielle)
@Entity
@Table(name = "communes", indexes = @Index(columnList = "departement_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class Commune extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String nom;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "departement_id")
    private Departement departement;

    private Double latitude;
    private Double longitude;
}

