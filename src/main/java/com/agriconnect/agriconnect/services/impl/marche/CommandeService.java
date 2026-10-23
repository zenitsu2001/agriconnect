package com.agriconnect.agriconnect.services.impl.marche;

import com.agriconnect.agriconnect.entity.marche.Commande;
import com.agriconnect.agriconnect.entity.marche.Produit;
import com.agriconnect.agriconnect.entity.metier.Utilisateur;
import com.agriconnect.agriconnect.enums.FrontCodes;
import com.agriconnect.agriconnect.enums.StatutCommande;
import com.agriconnect.agriconnect.enums.StatutProduit;
import com.agriconnect.agriconnect.exception.ApiException;
import com.agriconnect.agriconnect.repository.marche.CommandeRepository;
import com.agriconnect.agriconnect.repository.marche.ProduitRepository;
import com.agriconnect.agriconnect.repository.metier.AgriculteurRepository;
import com.agriconnect.agriconnect.security.UtilisateurCourant;
import com.agriconnect.agriconnect.services.dto.marche.MarcheProperties;
import com.agriconnect.agriconnect.services.dto.marche.request.CommandeRequest;
import com.agriconnect.agriconnect.services.dto.marche.request.CommandeStatutRequest;
import com.agriconnect.agriconnect.services.dto.marche.response.CommandeResponse;
import com.agriconnect.agriconnect.services.dto.parametrage.response.PageResponse;
import com.agriconnect.agriconnect.services.mapper.ProduitMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;

// ===== CommandeService.java =====
@Service
@RequiredArgsConstructor
public class CommandeService {

    private final UtilisateurCourant courant;
    private final AgriculteurRepository agriculteurs;
    private final ProduitRepository produits;
    private final CommandeRepository commandes;
    private final ProduitMapper mapper;
    private final MarcheProperties props;

    /** Passer commande. Le prix est celui du produit AU MOMENT de la commande, jamais celui du navigateur. */
    @Transactional
    public CommandeResponse creer(CommandeRequest request) {
        Utilisateur acheteur = courant.entite();
        Produit produit = produits.findById(request.produitId())
                .filter(p -> p.getStatut() == StatutProduit.EN_VENTE)
                .orElseThrow(() -> ApiException.notFound("errors.resource.notFound"));

        if (produit.getAgriculteur().getId().equals(acheteur.getId())) {
            throw ApiException.badRequest("errors.order.ownProduct");
        }
        if (request.quantite().compareTo(produit.getQuantite()) > 0) {
            throw ApiException.conflict("errors.order.quantityUnavailable");
        }

        Commande commande = mapper.toEntity(request);          // quantité, adresse, date souhaitée
        commande.setProduit(produit);
        commande.setAcheteur(acheteur);
        commande.setPrixUnitaire(produit.getPrix());
        commande.setMontantTotal(produit.getPrix().multiply(request.quantite()).setScale(2, RoundingMode.HALF_UP));
        // statut : EN_ATTENTE par défaut (@PrePersist). La quantité du produit ne baisse qu'à la CONFIRMATION.
        return mapper.toResponse(commandes.save(commande));
    }

    @Transactional(readOnly = true)
    public PageResponse<CommandeResponse> mesAchats(int page, int taille) {
        courant.entite();
        return PageResponse.de(commandes.findByAcheteurId(courant.id(), pageable(page, taille)).map(mapper::toResponse));
    }

    /** Commandes reçues sur les produits de l'agriculteur connecté. */
    @Transactional(readOnly = true)
    public PageResponse<CommandeResponse> recues(int page, int taille) {
        Long agriculteurId = idAgriculteur();
        return PageResponse.de(commandes.findByProduitAgriculteurId(agriculteurId, pageable(page, taille)).map(mapper::toResponse));
    }

    /** Confirmer, livrer ou annuler (transitions et droits : voir verifierTransition). */
    @Transactional
    public CommandeResponse changerStatut(Long id, CommandeStatutRequest request) {
        Utilisateur utilisateur = courant.entite();
        Commande commande = commandes.findById(id).orElseThrow(() -> ApiException.notFound("errors.resource.notFound"));

        boolean acheteur = commande.getAcheteur().getId().equals(utilisateur.getId());
        boolean vendeur = commande.getProduit().getAgriculteur().getId().equals(utilisateur.getId());
        if (!acheteur && !vendeur) throw ApiException.notFound("errors.resource.notFound");     // pas concerné : « introuvable »

        StatutCommande cible = FrontCodes.fromCode(StatutCommande.class, request.statut());    // code inconnu : 400
        verifierTransition(commande.getStatut(), cible, vendeur);

        if (cible == StatutCommande.CONFIRMEE) {
            Produit produit = verrouiller(commande);            // verrou : pas de double vente du même stock
            if (commande.getQuantite().compareTo(produit.getQuantite()) > 0) {
                throw ApiException.conflict("errors.order.quantityUnavailable");
            }
            produit.setQuantite(produit.getQuantite().subtract(commande.getQuantite()));
            if (produit.getQuantite().signum() == 0) produit.setStatut(StatutProduit.VENDU);
        } else if (cible == StatutCommande.ANNULEE && commande.getStatut() == StatutCommande.CONFIRMEE) {
            Produit produit = verrouiller(commande);            // commande confirmée puis annulée : le stock est rendu
            produit.setQuantite(produit.getQuantite().add(commande.getQuantite()));
            if (produit.getStatut() == StatutProduit.VENDU) produit.setStatut(StatutProduit.EN_VENTE);
        }

        commande.setStatut(cible);
        return mapper.toResponse(commande);
    }

    // ------------------------------------------------------------------ détails

    /**
     * Qui peut passer d'un statut à l'autre :
     *   en attente  → annulée (acheteur ou vendeur)  |  confirmée (vendeur)
     *   confirmée   → livrée ou annulée (vendeur seulement)
     *   livrée, annulée : définitifs
     */
    private void verifierTransition(StatutCommande actuel, StatutCommande cible, boolean vendeur) {
        boolean autorisee = switch (actuel) {
            case EN_ATTENTE -> cible == StatutCommande.ANNULEE || (vendeur && cible == StatutCommande.CONFIRMEE);
            case CONFIRMEE -> vendeur && (cible == StatutCommande.LIVREE || cible == StatutCommande.ANNULEE);
            case LIVREE, ANNULEE -> false;
        };
        if (!autorisee) throw ApiException.badRequest("errors.order.invalidTransition");
    }

    private Produit verrouiller(Commande commande) {
        return produits.verrouillerParId(commande.getProduit().getId())
                .orElseThrow(() -> ApiException.notFound("errors.resource.notFound"));
    }

    private Pageable pageable(int page, int taille) {
        return PageRequest.of(Math.max(page, 0), Math.min(Math.max(taille, 1), props.taillePageMax()),
                Sort.by(Sort.Direction.DESC, "dateCreation"));
    }

    private Long idAgriculteur() {
        courant.entite();
        Long id = courant.id();
        if (!agriculteurs.existsById(id)) throw ApiException.forbidden("errors.auth.forbidden");
        return id;
    }
}