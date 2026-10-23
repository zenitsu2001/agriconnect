package com.agriconnect.agriconnect.services.impl;
// ===== AbonnementService.java =====
import com.agriconnect.agriconnect.entity.abonnement.Abonnement;
import com.agriconnect.agriconnect.entity.abonnement.Paiement;
import com.agriconnect.agriconnect.entity.abonnement.PlanAbonnement;
import com.agriconnect.agriconnect.entity.metier.Utilisateur;
import com.agriconnect.agriconnect.repository.abonnement.AbonnementRepository;
import com.agriconnect.agriconnect.repository.abonnement.PlanAbonnementRepository;
import com.agriconnect.agriconnect.enums.*;
import com.agriconnect.agriconnect.exception.ApiException;
import com.agriconnect.agriconnect.services.dto.abonnement.request.SouscriptionRequest;
import com.agriconnect.agriconnect.services.dto.abonnement.response.PlanActuelResponse;
import com.agriconnect.agriconnect.services.dto.abonnement.response.SouscriptionResponse;
import com.agriconnect.agriconnect.services.mapper.AbonnementMapper;
import com.agriconnect.agriconnect.paiement.PaiementService;
import com.agriconnect.agriconnect.paiement.PaiementTraiteEvent;
import com.agriconnect.agriconnect.security.UtilisateurCourant;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AbonnementService {

    public static final String PLAN_GRATUIT = "gratuit";

    /** Fonctionnalités réservées aux abonnés payants (mêmes codes que les clés subscription.features.* d'Angular). */
    public static final Set<String> FONCTIONNALITES_PREMIUM = Set.of(
            "smartPresale", "aiAssistant", "boost", "whatsapp", "realtimeStats",
            "audioMessaging", "prioritySupport", "advancedDashboard", "dataExport");

    private final UtilisateurCourant courant;
    private final PlanAbonnementRepository plans;
    private final AbonnementRepository abonnements;
    private final PaiementService paiementService;
    private final AbonnementMapper mapper;

    // ------------------------------------------------------------- souscription

    @Transactional
    public SouscriptionResponse souscrire(SouscriptionRequest request) {
        Utilisateur utilisateur = courant.entite();
        PlanAbonnement plan = plans.findByCode(request.planCode())
                .filter(p -> Boolean.TRUE.equals(p.getActif()))
                .orElseThrow(() -> ApiException.badRequest("errors.subscription.planUnknown"));

        // Plan gratuit : pas de paiement
        if (plan.getPrix().signum() == 0) return souscrireGratuit(utilisateur, plan);

        if (request.modePaiement() == null || request.modePaiement().isBlank()) {
            throw ApiException.badRequest("errors.payment.modeRequired");
        }
        ModePaiement mode = FrontCodes.fromCode(ModePaiement.class, request.modePaiement());

        // Le montant est celui du PLAN en base : le client ne peut pas payer moins en modifiant la requête
        Paiement paiement = paiementService.initier(utilisateur, ObjetPaiement.ABONNEMENT, plan.getPrix(), mode,
                "AgriConnect - " + plan.getCode());

        Abonnement abonnement = new Abonnement();
        abonnement.setUtilisateur(utilisateur);
        abonnement.setPlan(plan);
        abonnement.setStatut(StatutAbonnement.EN_ATTENTE);   // activé seulement quand le paiement est confirmé
        abonnement.setRenouvellementAuto(false);
        abonnement.setPaiement(paiement);
        abonnements.save(abonnement);

        return new SouscriptionResponse(mapper.toDto(abonnement), mapper.toInitie(paiement));
    }

    private SouscriptionResponse souscrireGratuit(Utilisateur utilisateur, PlanAbonnement plan) {
        // Un utilisateur qui a déjà un abonnement actif (même premium) n'est pas « rétrogradé » par ce choix
        if (abonnements.existsByUtilisateurIdAndStatut(utilisateur.getId(), StatutAbonnement.ACTIF)) {
            return new SouscriptionResponse(null, null);
        }
        Abonnement abonnement = new Abonnement();
        abonnement.setUtilisateur(utilisateur);
        abonnement.setPlan(plan);
        abonnement.setStatut(StatutAbonnement.ACTIF);
        abonnement.setDateDebut(LocalDateTime.now());         // dateFin = null : sans limite
        abonnement.setRenouvellementAuto(false);
        abonnements.save(abonnement);
        return new SouscriptionResponse(mapper.toDto(abonnement), null);
    }

    // -------------------------------------------------------------- plan actuel

    /** Plan réellement en vigueur (le gratuit si aucun abonnement payant en cours). */
    @Transactional(readOnly = true)
    public PlanActuelResponse planActuel(Long utilisateurId) {
        List<Abonnement> enCours = abonnements.trouverEnCours(utilisateurId, LocalDateTime.now(), PageRequest.of(0, 1));
        if (enCours.isEmpty()) return new PlanActuelResponse(PLAN_GRATUIT, false, null, Set.of());

        Abonnement abonnement = enCours.get(0);
        boolean premium = abonnement.getPlan().getPrix().signum() > 0;
        return new PlanActuelResponse(abonnement.getPlan().getCode(), premium, abonnement.getDateFin(),
                premium ? FONCTIONNALITES_PREMIUM : Set.of());
    }

    @Transactional(readOnly = true)
    public PlanActuelResponse monPlan() {
        return planActuel(courant.id());
    }

    // ---------------------------------------------- réaction au résultat du paiement

    /** Appelé (dans la même transaction) quand un paiement devient REUSSI ou ECHOUE. */
    @EventListener
    public void surPaiementTraite(PaiementTraiteEvent evenement) {
        if (evenement.objet() != ObjetPaiement.ABONNEMENT) return;

        abonnements.findByPaiementId(evenement.paiementId()).ifPresent(abonnement -> {
            if (evenement.statut() == StatutPaiement.REUSSI) activer(abonnement);
            else if (evenement.statut() == StatutPaiement.ECHOUE) abonnement.setStatut(StatutAbonnement.ANNULE);
        });
    }

    private void activer(Abonnement abonnement) {
        if (abonnement.getStatut() == StatutAbonnement.ACTIF) return;          // déjà fait : on n'ajoute pas une 2e période

        LocalDateTime maintenant = LocalDateTime.now();
        // Les périodes s'ajoutent : si un abonnement payant court encore, le nouveau commence à sa fin.
        // (Pas de calcul au prorata : un passage du mensuel à l'annuel commence après le mois en cours.)
        LocalDateTime debut = abonnements.finLaPlusLointaine(abonnement.getUtilisateur().getId(), maintenant)
                .filter(fin -> fin.isAfter(maintenant)).orElse(maintenant);

        abonnement.setStatut(StatutAbonnement.ACTIF);
        abonnement.setDateDebut(debut);
        abonnement.setDateFin(switch (abonnement.getPlan().getPeriode()) {
            case MENSUEL -> debut.plusMonths(1);
            case ANNUEL -> debut.plusYears(1);
            case AUCUNE -> null;
        });
    }

    /** Marque comme expirés les abonnements dont la date de fin est dépassée (tâche planifiée). */
    @Transactional
    public int expirerTermines() {
        List<Abonnement> termines = abonnements.findByStatutAndDateFinBefore(StatutAbonnement.ACTIF, LocalDateTime.now());
        termines.forEach(a -> a.setStatut(StatutAbonnement.EXPIRE));
        return termines.size();
    }
}