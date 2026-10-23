package com.agriconnect.agriconnect.paiement;

// ===== PasserelleSimulee.java + SimulationStore.java =====  (développement SANS compte marchand)
import com.agriconnect.agriconnect.entity.abonnement.Paiement;
import com.agriconnect.agriconnect.enums.ModePaiement;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** Mémoire de la simulation : statut choisi pour chaque référence (perdu au redémarrage : développement uniquement). */
@Component
public class SimulationStore {
    private final Map<String, PasserellePaiement.StatutExterne> statuts = new ConcurrentHashMap<>();

    public void definir(String reference, PasserellePaiement.StatutExterne statut) { statuts.put(reference, statut); }

    public PasserellePaiement.StatutExterne statut(String reference) {
        return statuts.getOrDefault(reference, PasserellePaiement.StatutExterne.EN_ATTENTE);
    }
}

