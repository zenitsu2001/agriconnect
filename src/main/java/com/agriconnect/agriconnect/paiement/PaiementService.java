package com.agriconnect.agriconnect.paiement;

// ===== PaiementService.java =====
import com.agriconnect.agriconnect.entity.abonnement.Paiement;
import com.agriconnect.agriconnect.entity.metier.Utilisateur;
import com.agriconnect.agriconnect.enums.*;
import com.agriconnect.agriconnect.exception.ApiException;
import com.agriconnect.agriconnect.exception.paiement.PasserelleException;
import com.agriconnect.agriconnect.repository.abonnement.PaiementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaiementService {

    private final PaiementRepository paiements;
    private final PasserelleRegistre registre;
    private final PaiementProperties props;
    private final ApplicationEventPublisher evenements;

    /** Modes de paiement proposés (selon la configuration). */
    public List<ModePaiement> modesActifs() {
        return registre.modesActifs();
    }

    /**
     * Démarre un paiement : crée la session chez le prestataire et enregistre le paiement EN_ATTENTE.
     * Le MONTANT est toujours fourni par le serveur (prix du plan), jamais par le client.
     */
    @Transactional
    public Paiement initier(Utilisateur utilisateur, ObjetPaiement objet, BigDecimal montant,
                            ModePaiement mode, String description) {
        PasserellePaiement passerelle = registre.pour(mode);              // 400 si le mode n'est pas activé
        String reference = "PAY-" + UUID.randomUUID().toString().replace("-", "").toUpperCase();

        PasserellePaiement.SessionPaiement session;
        try {
            session = passerelle.creerSession(new PasserellePaiement.DemandeSession(
                    reference, montant, "XOF", description,
                    props.frontendUrl() + "/paiement/retour?ref=" + reference + "&statut=ok",
                    props.frontendUrl() + "/paiement/retour?ref=" + reference + "&statut=echec",
                    props.urlPubliqueServeur() + "/api/paiements/notifications/" + mode.getCode()));
        } catch (PasserelleException e) {
            log.error("Création de session impossible ({}) : {}", passerelle.fournisseur(), e.getMessage(), e);
            throw ApiException.badGateway("errors.payment.providerUnavailable");
        }

        Paiement paiement = new Paiement();
        paiement.setUtilisateur(utilisateur);
        paiement.setMontant(montant);
        paiement.setMode(mode);
        paiement.setObjet(objet);
        paiement.setReference(reference);
        paiement.setReferenceExterne(session.referenceExterne());
        paiement.setFournisseur(passerelle.fournisseur());
        paiement.setUrlPaiement(session.urlPaiement());
        return paiements.saveAndFlush(paiement);
    }

    /**
     * Statut d'un paiement pour la page de retour d'Angular. S'il est encore en attente, on interroge
     * le prestataire : l'abonnement est donc activé même si le webhook est en retard ou perdu.
     */
    @Transactional
    public Paiement statutPourUtilisateur(String reference, Long utilisateurId) {
        Paiement paiement = paiements.verrouillerParReference(reference)
                .filter(p -> p.getUtilisateur().getId().equals(utilisateurId))    // le paiement d'un autre = « introuvable »
                .orElseThrow(() -> ApiException.notFound("errors.resource.notFound"));
        return actualiser(paiement);
    }

    /** Notification du prestataire : signature vérifiée, puis statut redemandé au prestataire. */
    @Transactional
    public void traiterNotification(ModePaiement mode, String corps, Map<String, String> entetes) {
        registre.pour(mode).lireNotification(corps, entetes)
                .ifPresent(notification -> actualiserParReference(notification.reference()));
    }

    /** Met à jour un paiement à partir de sa référence (webhook, scrutation, rattrapage). */
    @Transactional
    public void actualiserParReference(String reference) {
        paiements.verrouillerParReference(reference).ifPresentOrElse(
                this::actualiser,
                () -> log.warn("Notification pour une référence inconnue : {}", reference));
    }

    /** Références des paiements restés en attente, pour la tâche de rattrapage. */
    @Transactional(readOnly = true)
    public List<String> referencesEnAttente(LocalDateTime apres, LocalDateTime avant) {
        return paiements.findByStatutAndDateCreationBetween(StatutPaiement.EN_ATTENTE, apres, avant)
                .stream().map(Paiement::getReference).toList();
    }

    /** Un paiement jamais abouti après la limite est abandonné (ECHOUE). */
    @Transactional
    public int expirerAnciens(LocalDateTime limite) {
        List<Paiement> anciens = paiements.findByStatutAndDateCreationBefore(StatutPaiement.EN_ATTENTE, limite);
        anciens.forEach(p -> terminer(p, StatutPaiement.ECHOUE));
        return anciens.size();
    }

    // ----------------------------------------------------------------- interne

    /** Demande le statut au prestataire et applique le résultat. Sans effet si le paiement est déjà terminé. */
    private Paiement actualiser(Paiement paiement) {
        if (paiement.getStatut() != StatutPaiement.EN_ATTENTE) return paiement;

        try {
            switch (registre.pour(paiement.getMode()).verifier(paiement)) {
                case REUSSI -> terminer(paiement, StatutPaiement.REUSSI);
                case ECHOUE -> terminer(paiement, StatutPaiement.ECHOUE);
                case EN_ATTENTE -> { /* rien : on réessaiera */ }
            }
        } catch (PasserelleException e) {
            log.warn("Vérification impossible pour {} : {}", paiement.getReference(), e.getMessage());
        }
        return paiement;
    }

    private void terminer(Paiement paiement, StatutPaiement statut) {
        paiement.setStatut(statut);
        if (statut == StatutPaiement.REUSSI) paiement.setDateConfirmation(LocalDateTime.now());
        // Événement synchrone : l'activation de l'abonnement se fait dans la MÊME transaction
        evenements.publishEvent(new PaiementTraiteEvent(paiement.getId(), paiement.getObjet(), statut));
    }
}