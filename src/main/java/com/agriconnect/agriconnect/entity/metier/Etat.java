package com.agriconnect.agriconnect.entity.metier;

import com.agriconnect.agriconnect.enums.Profil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

// ===== Etat.java =====
@Entity
@Table(name = "etat_agents")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class Etat extends Utilisateur {

    @Column(length = 150)
    private String institution;     // ex. ministère de l'Agriculture

    @Override public Profil getProfil() { return Profil.ETAT; }
}
