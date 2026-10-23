package com.agriconnect.agriconnect.services.impl.marche;

import com.agriconnect.agriconnect.entity.marche.Avis;
import com.agriconnect.agriconnect.entity.marche.Produit;
import com.agriconnect.agriconnect.entity.metier.Utilisateur;
import com.agriconnect.agriconnect.enums.StatutCommande;
import com.agriconnect.agriconnect.exception.ApiException;
import com.agriconnect.agriconnect.repository.marche.AvisRepository;
import com.agriconnect.agriconnect.repository.marche.CommandeRepository;
import com.agriconnect.agriconnect.repository.marche.ProduitRepository;
import com.agriconnect.agriconnect.security.UtilisateurCourant;
import com.agriconnect.agriconnect.services.dto.marche.MarcheProperties;
import com.agriconnect.agriconnect.services.dto.marche.request.AvisRequest;
import com.agriconnect.agriconnect.services.dto.marche.response.AvisResponse;
import com.agriconnect.agriconnect.services.mapper.ProduitMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// ===== AvisService.java =====
@Service
@RequiredArgsConstructor
public class AvisService {

    private final UtilisateurCourant courant;
    private final ProduitRepository produits;
    private final AvisRepository avis;
    private final CommandeRepository commandes;
    private final ProduitMapper mapper;
    private final MarcheProperties props;

    @Transactional(readOnly = true)
    public List<AvisResponse> lister(Long produitId) {
        courant.entite();
        if (!produits.existsById(produitId)) throw ApiException.notFound("errors.resource.notFound");
        return avis.findByProduitIdOrderByDateCreationDesc(produitId).stream().map(mapper::toResponse).toList();
    }

    @Transactional
    public AvisResponse creer(Long produitId, AvisRequest request) {
        Utilisateur auteur = courant.entite();
        Produit produit = produits.findById(produitId).orElseThrow(() -> ApiException.notFound("errors.resource.notFound"));

        if (produit.getAgriculteur().getId().equals(auteur.getId())) {
            throw ApiException.forbidden("errors.review.ownProduct");
        }
        // Avis « vérifiés » : seul un acheteur dont la commande est livrée peut noter (règle configurable)
        if (props.avisReservesAuxAcheteurs()
                && !commandes.existsByAcheteurIdAndProduitIdAndStatut(auteur.getId(), produitId, StatutCommande.LIVREE)) {
            throw ApiException.forbidden("errors.review.notBuyer");
        }
        if (avis.existsByProduitIdAndAuteurId(produitId, auteur.getId())) {
            throw ApiException.conflict("errors.review.alreadyExists");
        }

        Avis nouveau = mapper.toEntity(request);
        nouveau.setProduit(produit);
        nouveau.setAuteur(auteur);
        return mapper.toResponse(avis.save(nouveau));
    }
}
