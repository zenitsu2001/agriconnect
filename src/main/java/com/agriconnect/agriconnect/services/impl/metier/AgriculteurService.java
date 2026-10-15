package com.agriconnect.agriconnect.services.impl.metier;
import com.agriconnect.agriconnect.entity.metier.Agriculteur;
import com.agriconnect.agriconnect.entity.parametre.Commune;
import com.agriconnect.agriconnect.entity.parametre.Departement;
import com.agriconnect.agriconnect.entity.parametre.Localisation;
import com.agriconnect.agriconnect.entity.parametre.Region;
import com.agriconnect.agriconnect.exception.ApiException;
import com.agriconnect.agriconnect.repository.metier.AgriculteurRepository;
import com.agriconnect.agriconnect.repository.parametre.CommuneRepository;
import com.agriconnect.agriconnect.repository.parametre.DepartementRepository;
import com.agriconnect.agriconnect.repository.parametre.RegionRepository;
import com.agriconnect.agriconnect.services.dto.metier.request.UpdateAgriculteurRequest;
import com.agriconnect.agriconnect.services.dto.metier.response.ExploitationResponse;
import com.agriconnect.agriconnect.services.dto.parametrage.request.LocalisationRequest;
import com.agriconnect.agriconnect.services.mapper.UtilisateurMapper;
import com.agriconnect.agriconnect.security.UtilisateurCourant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AgriculteurService {

    private final UtilisateurCourant courant;
    private final AgriculteurRepository agriculteurs;
    private final RegionRepository regions;
    private final DepartementRepository departements;
    private final CommuneRepository communes;
    private final UtilisateurMapper mapper;

    @Transactional(readOnly = true)
    public ExploitationResponse exploitation() {
        return mapper.toExploitation(agriculteurCourant());
    }

    /** Alimente le calendrier et les alertes météo du tableau de bord. */
    @Transactional
    public ExploitationResponse mettreAJour(UpdateAgriculteurRequest request) {
        Agriculteur agriculteur = agriculteurCourant();
        if (request.nomExploitation() != null) {
            agriculteur.setNomExploitation(request.nomExploitation().trim());
        }
        if (request.localisation() != null) {
            agriculteur.setExploitation(construireLocalisation(request.localisation()));
        }
        return mapper.toExploitation(agriculteur);
    }

    private Agriculteur agriculteurCourant() {
        courant.entite();                                  // refuse un compte désactivé
        return agriculteurs.findById(courant.id())
                .orElseThrow(() -> ApiException.forbidden("errors.auth.forbidden"));   // connecté mais pas agriculteur
    }

    /** Vérifie la cohérence région / département / commune et complète les coordonnées et le libellé. */
    private Localisation construireLocalisation(LocalisationRequest r) {
        Region region = regions.findByCode(r.regionCode())
                .orElseThrow(() -> ApiException.badRequest("errors.location.regionUnknown"));

        Departement departement = null;
        if (r.departementId() != null) {
            departement = departements.findById(r.departementId())
                    .filter(d -> d.getRegion().getId().equals(region.getId()))
                    .orElseThrow(() -> ApiException.badRequest("errors.location.departmentMismatch"));
        }

        Commune commune = null;
        if (r.communeId() != null) {
            commune = communes.findById(r.communeId())
                    .orElseThrow(() -> ApiException.badRequest("errors.location.communeMismatch"));
            Departement deptCommune = commune.getDepartement();
            boolean coherent = deptCommune.getRegion().getId().equals(region.getId())
                    && (departement == null || deptCommune.getId().equals(departement.getId()));
            if (!coherent) throw ApiException.badRequest("errors.location.communeMismatch");
            if (departement == null) departement = deptCommune;
        }

        Double latitude = r.latitude();
        Double longitude = r.longitude();
        if ((latitude == null) != (longitude == null)) {
            throw ApiException.badRequest("errors.location.invalidCoordinates");
        }
        if (latitude == null && commune != null) {          // coordonnées de la commune à défaut
            latitude = commune.getLatitude();
            longitude = commune.getLongitude();
        }
        if (latitude != null && longitude != null
                && (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180)) {
            throw ApiException.badRequest("errors.location.invalidCoordinates");
        }

        String libelle = r.libelle() != null && !r.libelle().isBlank() ? r.libelle().trim()
                : commune != null ? commune.getNom()
                : departement != null ? departement.getNom()
                : region.getNom();

        return Localisation.builder()
                .region(region).departement(departement).commune(commune)
                .latitude(latitude).longitude(longitude).libelle(libelle)
                .build();
    }
}