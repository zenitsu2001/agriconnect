package com.agriconnect.agriconnect.services.impl.marche;


import com.agriconnect.agriconnect.entity.abonnement.Paiement;
import com.agriconnect.agriconnect.entity.marche.Boost;
import com.agriconnect.agriconnect.entity.marche.Produit;
import com.agriconnect.agriconnect.entity.metier.Utilisateur;
import com.agriconnect.agriconnect.enums.*;
import com.agriconnect.agriconnect.exception.ApiException;
import com.agriconnect.agriconnect.paiement.BoostProperties;
import com.agriconnect.agriconnect.paiement.PaiementService;
import com.agriconnect.agriconnect.paiement.PaiementTraiteEvent;
import com.agriconnect.agriconnect.repository.marche.BoostRepository;
import com.agriconnect.agriconnect.repository.marche.ProduitRepository;
import com.agriconnect.agriconnect.repository.metier.AgriculteurRepository;
import com.agriconnect.agriconnect.security.UtilisateurCourant;
import com.agriconnect.agriconnect.services.dto.marche.request.BoostRequest;
import com.agriconnect.agriconnect.services.dto.marche.response.BoostCommandeResponse;
import com.agriconnect.agriconnect.services.impl.metier.DroitsService;
import com.agriconnect.agriconnect.services.mapper.AbonnementMapper;
import com.agriconnect.agriconnect.services.mapper.ProduitMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

// ===== BoostService.java =====
@Service
@RequiredArgsConstructor
public class BoostService {

    private final UtilisateurCourant courant;
    private final AgriculteurRepository agriculteurs;
    private final ProduitRepository produits;
    private final BoostRepository boosts;
    private final PaiementService paiementService;
    private final DroitsService droits;
    private final ProduitMapper produitMapper;
    private final AbonnementMapper abonnementMapper;
    private final BoostProperties props;

    /** Met un produit en avant : gratuit (activé tout de suite) ou payant (activé à la confirmation du paiement). */
    @Transactional
    public BoostCommandeResponse demarrer(Long produitId, BoostRequest request) {
        Utilisateur utilisateur = courant.entite();
        Long agriculteurId = utilisateur.getId();
        if (!agriculteurs.existsById(agriculteurId)) throw ApiException.forbidden("errors.auth.forbidden");

        // Le produit doit appartenir à l'agriculteur connecté (sinon « introuvable »)
        Produit produit = produits.findByIdAndAgriculteurId(produitId, agriculteurId)
                .orElseThrow(() -> ApiException.notFound("errors.resource.notFound"));
        if (produit.getStatut() != StatutProduit.EN_VENTE) throw ApiException.badRequest("errors.boost.productNotForSale");
        if (props.premiumRequis()) droits.exiger(agriculteurId, "boost");

        LocalDateTime maintenant = LocalDateTime.now();
        Boost boost = new Boost();
        boost.setProduit(produit);
        boost.setMontant(props.prix());
        boost.setActif(false);
        boost.setDateDebut(maintenant);                        // dates provisoires : fixées à l'activation
        boost.setDateFin(maintenant);

        // Boost gratuit : actif immédiatement, sans paiement
        if (props.prix().signum() == 0) {
            activer(boost);
            boosts.save(boost);
            return new BoostCommandeResponse(produitMapper.toResponse(boost), null);
        }

        if (request == null || request.modePaiement() == null || request.modePaiement().isBlank()) {
            throw ApiException.badRequest("errors.payment.modeRequired");
        }
        ModePaiement mode = FrontCodes.fromCode(ModePaiement.class, request.modePaiement());
        Paiement paiement = paiementService.initier(utilisateur, ObjetPaiement.BOOST, props.prix(), mode,
                "AgriConnect - boost");
        boost.setPaiement(paiement);
        boosts.save(boost);
        return new BoostCommandeResponse(produitMapper.toResponse(boost), abonnementMapper.toInitie(paiement));
    }

    /** Réaction au résultat du paiement (même mécanisme que pour les abonnements). */
    @EventListener
    public void surPaiementTraite(PaiementTraiteEvent evenement) {
        if (evenement.objet() != ObjetPaiement.BOOST) return;
        boosts.findByPaiementId(evenement.paiementId()).ifPresent(boost -> {
            if (evenement.statut() == StatutPaiement.REUSSI) activer(boost);   // si ECHOUE : le boost reste inactif
        });
    }

    private void activer(Boost boost) {
        if (Boolean.TRUE.equals(boost.getActif())) return;                     // déjà activé : pas de 2e période
        LocalDateTime maintenant = LocalDateTime.now();
        // Un produit déjà boosté voit sa mise en avant PROLONGÉE
        LocalDateTime debut = boosts
                .findFirstByProduitIdAndActifTrueAndDateFinAfterOrderByDateFinDesc(boost.getProduit().getId(), maintenant)
                .map(Boost::getDateFin).orElse(maintenant);
        boost.setDateDebut(debut);
        boost.setDateFin(debut.plusDays(props.dureeJours()));
        boost.setActif(true);
    }
}