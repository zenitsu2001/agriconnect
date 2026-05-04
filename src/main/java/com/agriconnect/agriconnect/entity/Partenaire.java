package com.agriconnect.agriconnect.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "partenaires")
@DiscriminatorValue("PARTENAIRE")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Partenaire extends Utilisateur {

    @Column(name = "type_partenariat")
    private String typePartenariat;

    @Column(name = "domaine_expertise")
    private String domaineExpertise;

    // ── Offres publiées par le partenaire ─────────────────────────────────────
    @OneToMany(mappedBy = "partenaire", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Offre> offres = new ArrayList<>();
}