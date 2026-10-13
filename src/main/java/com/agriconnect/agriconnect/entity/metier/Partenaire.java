package com.agriconnect.agriconnect.entity.metier;

import com.agriconnect.agriconnect.enums.Profil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

// ===== Partenaire.java =====
@Entity
@Table(name = "partenaires")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class Partenaire extends Utilisateur {

    @Column(length = 150)
    private String organisation;

    @Override public Profil getProfil() { return Profil.PARTENAIRE; }
}