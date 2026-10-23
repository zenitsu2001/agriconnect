package com.agriconnect.agriconnect.services.impl.marche;

import com.agriconnect.agriconnect.entity.marche.Produit;
import com.agriconnect.agriconnect.entity.marche.ProduitVue;
import com.agriconnect.agriconnect.entity.metier.Utilisateur;
import com.agriconnect.agriconnect.enums.StatutProduit;
import com.agriconnect.agriconnect.repository.marche.ProduitRepository;
import com.agriconnect.agriconnect.repository.marche.ProduitVueRepository;
import com.agriconnect.agriconnect.security.UtilisateurCourant;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@RequiredArgsConstructor
public class VuesProduits {

    private final UtilisateurCourant courant;
    private final ProduitRepository produits;
    private final ProduitVueRepository vues;
    private final TransactionTemplate transaction;

    /**
     * Compte la vue pour l'utilisateur connecté : une fois par utilisateur et par produit,
     * jamais pour le propriétaire ni pour un produit qui n'est pas en vente.
     * Transaction indépendante, terminée AVANT la lecture du produit : la réponse affiche le nouveau total.
     */
    public void compter(Long produitId) {
        try {
            transaction.executeWithoutResult(statut -> enregistrer(produitId));
        } catch (DataIntegrityViolationException doublon) {
            // deux requêtes simultanées du même utilisateur : l'autre a déjà compté la vue
        }
    }

    private void enregistrer(Long produitId) {
        Utilisateur utilisateur = courant.entite();
        Produit produit = produits.findById(produitId).orElse(null);
        if (produit == null || produit.getStatut() != StatutProduit.EN_VENTE) return;   // detail() répondra « introuvable »
        if (produit.getAgriculteur().getId().equals(utilisateur.getId())) return;
        if (vues.existsByProduitIdAndUtilisateurId(produitId, utilisateur.getId())) return;

        vues.save(ProduitVue.builder().produit(produit).utilisateur(utilisateur).build());
        produits.incrementerVues(produitId);          // incrément atomique : pas de vue perdue en cas d'accès simultanés
    }
}