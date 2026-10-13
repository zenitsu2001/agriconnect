package com.agriconnect.agriconnect.repository.metier;

import com.agriconnect.agriconnect.entity.metier.Agriculteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AgriculteurRepository extends JpaRepository<Agriculteur, Long> {
    List<Agriculteur> findByExploitation_Region_Id(Long regionId);
    long countByExploitation_Region_Id(Long regionId);

    interface CompteParRegion { Long getRegionId(); Long getTotal(); }

    @Query("select a.exploitation.region.id as regionId, count(a) as total " +
            "from Agriculteur a where a.exploitation.region is not null group by a.exploitation.region.id")
    List<CompteParRegion> compterParRegion();
}