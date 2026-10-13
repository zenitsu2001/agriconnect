package com.agriconnect.agriconnect.repository.calendrier;

import com.agriconnect.agriconnect.entity.calendrier.CalculPrevente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalculPreventeRepository extends JpaRepository<CalculPrevente, Long> {
    List<CalculPrevente> findByAgriculteurIdOrderByDateCreationDesc(Long agriculteurId);
}
