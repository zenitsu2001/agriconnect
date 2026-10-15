package com.agriconnect.agriconnect.services.config;// ===== ReferenceResolver.java =====  (identifiant ou code reçu du frontend -> entité)
import com.agriconnect.agriconnect.entity.marche.Produit;
import com.agriconnect.agriconnect.entity.marche.ProduitReference;
import com.agriconnect.agriconnect.entity.metier.Utilisateur;
import com.agriconnect.agriconnect.entity.parametre.Commune;
import com.agriconnect.agriconnect.entity.parametre.Departement;
import com.agriconnect.agriconnect.entity.parametre.LangueSupportee;
import com.agriconnect.agriconnect.entity.parametre.Region;
import com.agriconnect.agriconnect.repository.marche.ProduitReferenceRepository;
import com.agriconnect.agriconnect.repository.marche.ProduitRepository;
import com.agriconnect.agriconnect.repository.metier.UtilisateurRepository;
import com.agriconnect.agriconnect.repository.parametre.CommuneRepository;
import com.agriconnect.agriconnect.repository.parametre.DepartementRepository;
import com.agriconnect.agriconnect.repository.parametre.LangueSupporteeRepository;
import com.agriconnect.agriconnect.repository.parametre.RegionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReferenceResolver {

    private final RegionRepository regions;
    private final DepartementRepository departements;
    private final CommuneRepository communes;
    private final ProduitReferenceRepository produitsReference;
    private final LangueSupporteeRepository langues;
    private final UtilisateurRepository utilisateurs;
    private final ProduitRepository produits;

    public Region region(String code) {
        return code == null ? null : regions.findByCode(code).orElseThrow(() -> introuvable("Région", code));
    }
    public Departement departement(Long id) {
        return id == null ? null : departements.findById(id).orElseThrow(() -> introuvable("Département", id));
    }
    public Commune commune(Long id) {
        return id == null ? null : communes.findById(id).orElseThrow(() -> introuvable("Commune", id));
    }
    public ProduitReference produitReference(String code) {
        return code == null ? null
                : produitsReference.findByCode(code).orElseThrow(() -> introuvable("Produit de référence", code));
    }
    public LangueSupportee langue(String code) {
        return code == null ? null
                : langues.findByCodeAndActifTrue(code).orElseThrow(() -> introuvable("Langue active", code));
    }
    public Utilisateur utilisateur(Long id) {
        return id == null ? null : utilisateurs.findById(id).orElseThrow(() -> introuvable("Utilisateur", id));
    }
    public Produit produit(Long id) {
        return id == null ? null : produits.findById(id).orElseThrow(() -> introuvable("Produit", id));
    }

    private static EntityNotFoundException introuvable(String type, Object cle) {
        return new EntityNotFoundException(type + " introuvable : " + cle);
    }
}