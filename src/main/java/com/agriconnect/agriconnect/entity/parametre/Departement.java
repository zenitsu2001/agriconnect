package com.agriconnect.agriconnect.entity.parametre;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

// ===== Departement.java =====  (46 départements)
@Entity
@Table(name = "departements", indexes = @Index(columnList = "region_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Departement extends BaseEntity {

    @Column(nullable = false, length = 80)
    private String nom;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "region_id")
    private Region region;
}
