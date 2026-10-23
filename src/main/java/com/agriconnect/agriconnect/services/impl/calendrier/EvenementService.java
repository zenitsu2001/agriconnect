package com.agriconnect.agriconnect.services.impl.calendrier;
import com.agriconnect.agriconnect.entity.calendrier.Evenement;
import com.agriconnect.agriconnect.enums.TypeEvenement;
import com.agriconnect.agriconnect.exception.ApiException;
import com.agriconnect.agriconnect.repository.calendrier.EvenementRepository;
import com.agriconnect.agriconnect.repository.metier.AgriculteurRepository;
import com.agriconnect.agriconnect.security.UtilisateurCourant;
import com.agriconnect.agriconnect.services.dto.calendrier.request.EvenementEtatRequest;
import com.agriconnect.agriconnect.services.dto.calendrier.request.EvenementRequest;
import com.agriconnect.agriconnect.services.dto.calendrier.response.EvenementResponse;
import com.agriconnect.agriconnect.services.mapper.CalendrierMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvenementService {

    /** Période maximale d'une recherche (≈ 3 mois) : évite de charger tout l'historique d'un coup. */
    private static final long DUREE_MAX_JOURS = 92;

    private final UtilisateurCourant courant;
    private final AgriculteurRepository agriculteurs;
    private final EvenementRepository evenements;
    private final CalendrierMapper mapper;

    /** Événements d'une période (le calendrier demande le mois affiché : du 1er au dernier jour). */
    @Transactional(readOnly = true)
    public List<EvenementResponse> lister(LocalDate debut, LocalDate fin) {
        if (debut.isAfter(fin) || ChronoUnit.DAYS.between(debut, fin) > DUREE_MAX_JOURS) {
            throw ApiException.badRequest("errors.request.invalidRange");
        }
        return evenements
                .findByProprietaireIdAndDateEvenementBetweenOrderByDateEvenement(idAgriculteur(), debut, fin)
                .stream().map(mapper::toResponse).toList();
    }

    /** Prochains événements non terminés, à partir d'aujourd'hui (panneau « Événements à venir »). */
    @Transactional(readOnly = true)
    public List<EvenementResponse> aVenir(int limite) {
        int taille = Math.min(Math.max(limite, 1), 20);        // entre 1 et 20, quoi que demande le client
        return evenements
                .findByProprietaireIdAndDateEvenementGreaterThanEqualAndTermineFalseOrderByDateEvenement(
                        idAgriculteur(), LocalDate.now(),  PageRequest.of(0, taille))
                .stream().map(mapper::toResponse).toList();
    }

    @Transactional
    public EvenementResponse creer(EvenementRequest request) {
        Long agriculteurId = idAgriculteur();

        Evenement evenement = mapper.toEntity(request);          // code de type inconnu -> 400 (CodeInconnuException)
        verifierType(evenement);
        evenement.setTitre(evenement.getTitre().trim());
        // Le propriétaire vient du JETON, jamais du corps de la requête.
        // getReferenceById évite de recharger l'agriculteur : on n'a besoin que de son identifiant.
        evenement.setProprietaire(agriculteurs.getReferenceById(agriculteurId));

        return mapper.toResponse(evenements.save(evenement));
    }

    @Transactional
    public EvenementResponse modifier(Long id, EvenementRequest request) {
        Evenement evenement = trouver(id);
        mapper.mettreAJour(request, evenement);
        verifierType(evenement);
        evenement.setTitre(evenement.getTitre().trim());
        return mapper.toResponse(evenement);                     // entité gérée : enregistrée à la fin de la transaction
    }

    /** Coche ou décoche « terminé ». */
    @Transactional
    public EvenementResponse definirEtat(Long id, EvenementEtatRequest request) {
        Evenement evenement = trouver(id);
        evenement.setTermine(request.termine());
        return mapper.toResponse(evenement);
    }

    @Transactional
    public void supprimer(Long id) {
        evenements.delete(trouver(id));
    }

    // ---------------------------------------------------------------- détails

    /** Les événements « météo » sont calculés par l'application : l'utilisateur ne peut pas en créer. */
    private void verifierType(Evenement evenement) {
        if (evenement.getType() == TypeEvenement.METEO) {
            throw ApiException.badRequest("errors.event.typeNotAllowed");
        }
    }

    /** Événement de l'agriculteur connecté, sinon 404 (même réponse qu'un identifiant inexistant). */
    private Evenement trouver(Long id) {
        return evenements.findByIdAndProprietaireId(id, idAgriculteur())
                .orElseThrow(() -> ApiException.notFound("errors.resource.notFound"));
    }

    private Long idAgriculteur() {
        courant.entite();                                        // refuse un compte désactivé
        Long id = courant.id();
        if (!agriculteurs.existsById(id)) {                      // connecté, mais pas agriculteur
            throw ApiException.forbidden("errors.auth.forbidden");
        }
        return id;
    }

    /** Événements importants des prochains jours, pour le bloc « Alertes & Rappels » du tableau de bord. */
    @Transactional(readOnly = true)
    public List<EvenementResponse> rappels(int jours, int limite) {
        int fenetre = Math.min(Math.max(jours, 1), 30);       // entre 1 et 30 jours, quoi que demande le client
        int taille = Math.min(Math.max(limite, 1), 20);       // entre 1 et 20 résultats
        LocalDate aujourdhui = LocalDate.now();

        return evenements
                .trouverRappels(idAgriculteur(), aujourdhui, aujourdhui.plusDays(fenetre), PageRequest.of(0, taille))
                .stream().map(mapper::toResponse).toList();
    }
}