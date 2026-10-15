package com.agriconnect.agriconnect.services.dto.metier.response;

/** Résumé d'un utilisateur (contact, auteur, acheteur...) : champs communs uniquement. */
public record UtilisateurResume(Long id, String nom, String prenom, String photoProfilUrl, String profil) {}
