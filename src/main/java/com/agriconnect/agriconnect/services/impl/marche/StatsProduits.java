package com.agriconnect.agriconnect.services.impl.marche;

import com.agriconnect.agriconnect.repository.marche.AvisRepository;
import com.agriconnect.agriconnect.repository.marche.BoostRepository;
import com.agriconnect.agriconnect.services.dto.marche.response.ProduitStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

// ===== StatsProduits.java =====
@Component
@RequiredArgsConstructor
public class StatsProduits {

    private final AvisRepository avis;
    private final BoostRepository boosts;

    /** Note moyenne, nombre d'avis et boost en cours de plusieurs produits : 2 requêtes au total, quelle que soit la taille de la liste. */
    public Map<Long, ProduitStats> pour(Collection<Long> ids) {
        if (ids.isEmpty()) return Map.of();

        Map<Long, AvisRepository.StatsAvis> notes = avis.statistiquesParProduits(ids).stream()
                .collect(Collectors.toMap(AvisRepository.StatsAvis::getProduitId, Function.identity()));
        Set<Long> boostes = new HashSet<>(boosts.produitsBoostes(ids, LocalDateTime.now()));

        Map<Long, ProduitStats> resultat = new HashMap<>();
        for (Long id : ids) {
            AvisRepository.StatsAvis stats = notes.get(id);
            Double moyenne = stats == null ? null : Math.round(stats.getMoyenne() * 10) / 10.0;   // une décimale
            resultat.put(id, new ProduitStats(moyenne, stats == null ? 0L : stats.getNombre(), boostes.contains(id)));
        }
        return resultat;
    }
}