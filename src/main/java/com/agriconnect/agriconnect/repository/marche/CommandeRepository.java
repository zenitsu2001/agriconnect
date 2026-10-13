package com.agriconnect.agriconnect.repository.marche;

import com.agriconnect.agriconnect.entity.marche.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByAcheteurIdOrderByDateCreationDesc(Long acheteurId);
    List<Commande> findByProduitAgriculteurIdOrderByDateCreationDesc(Long agriculteurId);   // commandes reçues
}