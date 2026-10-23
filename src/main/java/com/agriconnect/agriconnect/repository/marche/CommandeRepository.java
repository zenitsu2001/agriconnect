package com.agriconnect.agriconnect.repository.marche;

import com.agriconnect.agriconnect.entity.marche.Commande;
import com.agriconnect.agriconnect.enums.StatutCommande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByAcheteurIdOrderByDateCreationDesc(Long acheteurId);
    List<Commande> findByProduitAgriculteurIdOrderByDateCreationDesc(Long agriculteurId);   // commandes reçues

    Page<Commande> findByAcheteurId(Long acheteurId, Pageable pageable);
    Page<Commande> findByProduitAgriculteurId(Long agriculteurId, Pageable pageable);
    boolean existsByAcheteurIdAndProduitIdAndStatut(Long acheteurId, Long produitId, StatutCommande statut);
}