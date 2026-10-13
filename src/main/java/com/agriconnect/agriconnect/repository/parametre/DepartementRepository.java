package com.agriconnect.agriconnect.repository.parametre;
import com.agriconnect.agriconnect.entity.parametre.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// ===== Référentiel géographique =====

public interface DepartementRepository extends JpaRepository<Departement, Long> {
    List<Departement> findByRegionIdOrderByNom(Long regionId);
}
