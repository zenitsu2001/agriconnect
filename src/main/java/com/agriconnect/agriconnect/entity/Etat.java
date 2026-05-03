package com.agriconnect.agriconnect.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "etats")
@DiscriminatorValue("ETAT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Etat extends Utilisateur {

    private String region;

    private String departement;
}