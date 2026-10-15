package com.agriconnect.agriconnect.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
// ===== Profil.java =====  (mêmes valeurs que le formulaire d'inscription Angular)
@Getter @RequiredArgsConstructor
public enum Profil implements FrontCode {
    AGRICULTEUR("agriculteur"), CLIENT("client"), INVESTISSEUR("investisseur"),
    PARTENAIRE("partenaire"), ETAT("etat");
    private final String code;
}