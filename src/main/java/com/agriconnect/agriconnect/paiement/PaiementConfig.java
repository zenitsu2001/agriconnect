package com.agriconnect.agriconnect.paiement;

// ===== PaiementConfig.java =====  (construit les passerelles actives)
import com.agriconnect.agriconnect.enums.ModePaiement;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.util.EnumMap;
import java.util.Map;

@Configuration
@EnableScheduling                                     // tâches planifiées (rattrapage des paiements, expiration des abonnements)

@EnableConfigurationProperties({PaiementProperties.class, BoostProperties.class})
public class PaiementConfig {

    @Bean
    PasserelleRegistre passerelleRegistre(PaiementProperties props, SimulationStore store) {
        Map<ModePaiement, PasserellePaiement> passerelles = new EnumMap<>(ModePaiement.class);
        ajouter(passerelles, ModePaiement.WAVE, props.wave(), props, store);
        ajouter(passerelles, ModePaiement.ORANGE_MONEY, props.orange(), props, store);
        // ModePaiement.CARTE : pas de prestataire pour l'instant -> mode non proposé
        return new PasserelleRegistre(passerelles);
    }

    private void ajouter(Map<ModePaiement, PasserellePaiement> cible, ModePaiement mode,
                         PaiementProperties.Fournisseur config, PaiementProperties props, SimulationStore store) {
        if (config == null || !config.actif()) return;

        if ("reel".equalsIgnoreCase(config.mode())) {
            cible.put(mode, switch (mode) {
                case WAVE -> new WaveGateway(config);
                case ORANGE_MONEY -> new OrangeMoneyGateway();
                default -> throw new IllegalStateException("Pas de passerelle réelle pour " + mode);
            });
            return;
        }

        // Garde-fou : on ne démarre pas en simulation par accident en production
        if (!props.simulationActive()) {
            throw new IllegalStateException("Le mode simulation est désactivé (agriconnect.paiement.simulation-active=false) : "
                    + "configurez le mode « reel » pour " + mode);
        }
        cible.put(mode, new PasserelleSimulee(mode, store, props.frontendUrl()));
    }
}