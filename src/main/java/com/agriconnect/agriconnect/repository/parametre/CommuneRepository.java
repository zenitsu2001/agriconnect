package com.agriconnect.agriconnect.repository.parametre;
import com.agriconnect.agriconnect.entity.parametre.Commune;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface CommuneRepository extends JpaRepository<Commune, Long> {
    List<Commune> findByDepartementIdOrderByNom(Long departementId);

    /** Suggestions de communes pour le formulaire de localisation (région choisie, saisie partielle). */
    List<Commune> findTop20ByNomContainingIgnoreCaseAndDepartementRegionIdOrderByNom(String nom, Long regionId);
}