package com.agriconnect.agriconnect.entity.parametre;

import com.agriconnect.agriconnect.enums.Profil;
import jakarta.persistence.*;
import lombok.*;

// ParametreProfil.java : langue par défaut de chaque acteur
@Entity @Table(name = "parametres_profil")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParametreProfil {
    @Id @Enumerated(EnumType.STRING) @Column(length = 15) private Profil profil;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "langue_defaut")
    private LangueSupportee langueParDefaut;
}