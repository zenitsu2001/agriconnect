package com.agriconnect.agriconnect.entity.metier;

import com.agriconnect.agriconnect.enums.Profil;
import com.agriconnect.agriconnect.enums.TypeClient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

// ===== Client.java =====  (supermarché, restaurateur...)
@Entity
@Table(name = "clients")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class Client extends Utilisateur {

    @Column(length = 150)
    private String nomEntreprise;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TypeClient typeClient;

    @Override public Profil getProfil() { return Profil.CLIENT; }
}