package com.agriconnect.agriconnect.services.impl.marche;

import com.agriconnect.agriconnect.entity.marche.Produit;
import com.agriconnect.agriconnect.entity.parametre.Region;
import com.agriconnect.agriconnect.enums.StatutProduit;
import com.agriconnect.agriconnect.exception.ApiException;
import com.agriconnect.agriconnect.repository.marche.ProduitRepository;
import com.agriconnect.agriconnect.repository.metier.AgriculteurRepository;
import com.agriconnect.agriconnect.repository.parametre.RegionRepository;
import com.agriconnect.agriconnect.security.UtilisateurCourant;
import com.agriconnect.agriconnect.services.dto.marche.MarcheProperties;
import com.agriconnect.agriconnect.services.dto.marche.request.ProduitRequest;
import com.agriconnect.agriconnect.services.dto.marche.response.ProduitResponse;
import com.agriconnect.agriconnect.services.dto.marche.response.ProduitStats;
import com.agriconnect.agriconnect.services.dto.parametrage.response.PageResponse;
import com.agriconnect.agriconnect.services.mapper.ProduitMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

// ===== ProduitService.java =====
@Service
@RequiredArgsConstructor
public class ProduitService {

    private final UtilisateurCourant courant;
    private final AgriculteurRepository agriculteurs;
    private final ProduitRepository produits;
    private final RegionRepository regions;
    private final StatsProduits stats;
    private final FichierService fichiers;
    private final ProduitMapper mapper;
    private final MarcheProperties props;

    // --------------------------------------------------------------- lecture

    /** Liste du marché : produits EN_VENTE, boostés en tête, puis selon le tri choisi. */
    @Transactional(readOnly = true)
    public PageResponse<ProduitResponse> rechercher(String q, String regionCode, String tri, int page, int taille) {
        courant.entite();                                           // compte actif

        Long regionId = null;
        if (regionCode != null && !regionCode.isBlank()) {
            regionId = regions.findByCode(regionCode).map(Region::getId)
                    .orElseThrow(() -> ApiException.badRequest("errors.location.regionUnknown"));
        }
        Pageable pageable = PageRequest.of(Math.max(page, 0), taillePage(taille), triDe(tri));
        return versPage(produits.rechercher(q == null ? "" : q.trim(), regionId, LocalDateTime.now(), pageable));
    }

    /** Produits de l'agriculteur connecté (sauf ceux qu'il a retirés). */
    @Transactional(readOnly = true)
    public PageResponse<ProduitResponse> mesProduits(int page, int taille) {
        Long agriculteurId = idAgriculteur();
        Pageable pageable = PageRequest.of(Math.max(page, 0), taillePage(taille), Sort.by(Sort.Direction.DESC, "dateCreation"));
        return versPage(produits.findByAgriculteurIdAndStatutNot(agriculteurId, StatutProduit.RETIRE, pageable));
    }

    /** Détail d'un produit. Le compteur de vues augmente, sauf pour le propriétaire. */
    @Transactional(readOnly = true)
    public ProduitResponse detail(Long id) {
        Long utilisateurId = courant.entite().getId();
        Produit produit = produits.findById(id).orElseThrow(() -> ApiException.notFound("errors.resource.notFound"));

        boolean proprietaire = produit.getAgriculteur().getId().equals(utilisateurId);
        // Un produit retiré ou vendu n'est visible que de son propriétaire (les autres : « introuvable »)
        if (!proprietaire && produit.getStatut() != StatutProduit.EN_VENTE) {
            throw ApiException.notFound("errors.resource.notFound");
        }
        return mapper.toResponse(produit, stats.pour(List.of(id)).get(id));
    }

    // -------------------------------------------------------------- écriture

    @Transactional
    public ProduitResponse creer(ProduitRequest request) {
        Long agriculteurId = idAgriculteur();
        validerImages(request.images());

        Produit produit = mapper.toEntity(request);
        produit.setNom(produit.getNom().trim());
        // Le propriétaire vient du JETON, jamais du corps de la requête
        produit.setAgriculteur(agriculteurs.getReferenceById(agriculteurId));

        return mapper.toResponse(produits.save(produit), new ProduitStats(null, 0L, false));
    }

    @Transactional
    public ProduitResponse modifier(Long id, ProduitRequest request) {
        Produit produit = produitDuProprietaire(id);
        validerImages(request.images());

        mapper.mettreAJour(request, produit);              // statut et nombre de vues ne sont pas touchés
        produit.setNom(produit.getNom().trim());
        if (produit.getStatut() == StatutProduit.VENDU && produit.getQuantite().signum() > 0) {
            produit.setStatut(StatutProduit.EN_VENTE);     // stock réapprovisionné : le produit repart sur le marché
        }
        return mapper.toResponse(produit, stats.pour(List.of(id)).get(id));
    }

    /** « Suppression » : le produit est retiré du marché mais conservé (historique des commandes). */
    @Transactional
    public void retirer(Long id) {
        produitDuProprietaire(id).setStatut(StatutProduit.RETIRE);
    }

    // ----------------------------------------------------------------- détails

    private PageResponse<ProduitResponse> versPage(Page<Produit> page) {
        // Une seule série de requêtes pour les statistiques de TOUTE la page (pas une par produit)
        Map<Long, ProduitStats> statistiques = stats.pour(page.getContent().stream().map(Produit::getId).toList());
        return PageResponse.de(page.map(p -> mapper.toResponse(p, statistiques.get(p.getId()))));
    }

    /** Codes de tri d'Angular → tri de la base. */
    private Sort triDe(String tri) {
        return switch (tri == null ? "recent" : tri) {
            case "recent" -> Sort.by(Sort.Direction.DESC, "dateCreation");
            case "priceLow" -> Sort.by(Sort.Direction.ASC, "prix");
            case "priceHigh" -> Sort.by(Sort.Direction.DESC, "prix");
            case "popular" -> Sort.by(Sort.Direction.DESC, "nombreVues");
            default -> throw ApiException.badRequest("errors.request.invalidCode");
        };
    }

    private int taillePage(int taille) {
        return Math.min(Math.max(taille, 1), props.taillePageMax());
    }

    /** Une image doit avoir été envoyée sur NOTRE serveur : pas d'adresse externe. */
    private void validerImages(List<String> images) {
        if (images == null) return;
        for (String url : images) {
            if (!fichiers.estImageStockee(url)) throw ApiException.badRequest("errors.product.imageInvalid");
        }
    }

    /** Produit appartenant à l'agriculteur connecté, sinon « introuvable » (même réponse qu'un identifiant inexistant). */
    private Produit produitDuProprietaire(Long id) {
        return produits.findByIdAndAgriculteurId(id, idAgriculteur())
                .filter(p -> p.getStatut() != StatutProduit.RETIRE)
                .orElseThrow(() -> ApiException.notFound("errors.resource.notFound"));
    }

    private Long idAgriculteur() {
        courant.entite();
        Long id = courant.id();
        if (!agriculteurs.existsById(id)) throw ApiException.forbidden("errors.auth.forbidden");
        return id;
    }
}