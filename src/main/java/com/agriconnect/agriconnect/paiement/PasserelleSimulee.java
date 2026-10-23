package com.agriconnect.agriconnect.paiement;

// ===== PasserelleSimulee.java + SimulationStore.java =====  (développement SANS compte marchand)
import com.agriconnect.agriconnect.entity.abonnement.Paiement;
import com.agriconnect.agriconnect.enums.ModePaiement;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** « Faux prestataire » : renvoie vers une page de simulation de notre propre site Angular. */
public class PasserelleSimulee implements PasserellePaiement {

    private final ModePaiement mode;
    private final SimulationStore store;
    private final String frontendUrl;

    public PasserelleSimulee(ModePaiement mode, SimulationStore store, String frontendUrl) {
        this.mode = mode;
        this.store = store;
        this.frontendUrl = frontendUrl;
    }

    @Override public ModePaiement mode() { return mode; }
    @Override public String fournisseur() { return "simulation"; }

    @Override
    public SessionPaiement creerSession(DemandeSession demande) {
        store.definir(demande.reference(), StatutExterne.EN_ATTENTE);
        String url = frontendUrl + "/paiement/simulation?ref=" + demande.reference() + "&mode=" + mode.getCode();
        return new SessionPaiement("SIM-" + demande.reference(), url);
    }

    @Override
    public StatutExterne verifier(Paiement paiement) {
        return store.statut(paiement.getReference());
    }

    @Override
    public Optional<NotificationPaiement> lireNotification(String corps, Map<String, String> entetes) {
        return Optional.empty();                      // pas de webhook en simulation
    }

}
