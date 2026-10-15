package com.agriconnect.agriconnect.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
// ===== PeriodeAbonnement.java =====  (AUCUNE = "" comme dans le modèle Plan d'Angular)
@Getter @RequiredArgsConstructor
public enum PeriodeAbonnement implements FrontCode {
    AUCUNE(""), MENSUEL("month"), ANNUEL("year");
    private final String code;
}