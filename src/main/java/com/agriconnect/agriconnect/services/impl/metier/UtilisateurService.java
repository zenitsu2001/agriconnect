package com.agriconnect.agriconnect.services.impl.metier;

import com.agriconnect.agriconnect.entity.metier.Utilisateur;
import com.agriconnect.agriconnect.exception.ApiException;
import com.agriconnect.agriconnect.repository.metier.UtilisateurRepository;
import com.agriconnect.agriconnect.repository.parametre.LangueSupporteeRepository;
import com.agriconnect.agriconnect.security.UtilisateurCourant;
import com.agriconnect.agriconnect.services.dto.metier.Saisies;
import com.agriconnect.agriconnect.services.dto.metier.request.UpdateProfilRequest;
import com.agriconnect.agriconnect.services.dto.metier.response.UtilisateurResponse;
import com.agriconnect.agriconnect.services.impl.marche.FichierService;
import com.agriconnect.agriconnect.services.mapper.UtilisateurMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UtilisateurService {

    private final UtilisateurCourant courant;
    private final UtilisateurRepository utilisateurs;
    private final LangueSupporteeRepository langues;
    private final UtilisateurMapper mapper;
    private final FichierService fichiers ;

    @Transactional(readOnly = true)
    public UtilisateurResponse moi() {
        return mapper.toResponse(courant.entite());
    }

    @Transactional
    public UtilisateurResponse mettreAJourProfil(UpdateProfilRequest request) {
        Utilisateur utilisateur = courant.entite();

        if (request.langue() != null) {                    // validée ici : une langue inconnue est une erreur 400
            langues.findByCodeAndActifTrue(request.langue().trim().toLowerCase())
                    .orElseThrow(() -> ApiException.badRequest("errors.language.unsupported"));
        }
        String telephone = Saisies.telephone(request.telephone());
        if (telephone != null) {
            boolean pris = utilisateurs.findByTelephone(telephone)
                    .filter(autre -> !autre.getId().equals(utilisateur.getId())).isPresent();
            if (pris) throw ApiException.conflict("errors.register.phoneTaken");
        }
        if (request.photoProfilUrl() != null && !request.photoProfilUrl().isBlank()
                && !fichiers.estImageStockee(request.photoProfilUrl())) {
            throw ApiException.badRequest("errors.file.invalid");
        }

        mapper.mettreAJour(request, utilisateur);          // champs null ignorés
        if (telephone != null) utilisateur.setTelephone(telephone);
        return mapper.toResponse(utilisateur);
    }

}