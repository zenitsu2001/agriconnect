package com.agriconnect.agriconnect.entity.metier;

import com.agriconnect.agriconnect.entity.parametre.Localisation;
import com.agriconnect.agriconnect.enums.Profil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

// ===== Agriculteur.java =====
@Entity
@Table(name = "agriculteurs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class Agriculteur extends Utilisateur {

    @Column(length = 150)
    private String nomExploitation;

    @Embedded
    private Localisation exploitation;

    @Override public Profil getProfil() { return Profil.AGRICULTEUR; }
}