package com.agriconnect.agriconnect.repository.parametre;
import com.agriconnect.agriconnect.entity.parametre.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

// ===== Référentiel géographique =====

public interface RegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findByCode(String code);
    List<Region> findAllByOrderByNomAsc();
}