package com.agriconnect.agriconnect.entity.metier;

import com.agriconnect.agriconnect.enums.Profil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

// ===== Investisseur.java =====
@Entity
@Table(name = "investisseurs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class Investisseur extends Utilisateur {

    @Column(length = 150)
    private String organisation;

    @Override public Profil getProfil() { return Profil.INVESTISSEUR; }
}