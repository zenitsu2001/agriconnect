package com.agriconnect.agriconnect.services.dto.metier;

// ===== Saisies.java =====  (normalisation des champs saisis, package service)
public final class Saisies {
    private Saisies() {}

    public static String email(String valeur) {
        return valeur == null ? null : valeur.trim().toLowerCase();
    }

    public static String telephone(String valeur) {
        return valeur == null ? null : valeur.replaceAll("[\\s.-]", "");
    }
}