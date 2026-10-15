package com.agriconnect.agriconnect.services.mapper;

import com.agriconnect.agriconnect.entity.abonnement.Abonnement;
import com.agriconnect.agriconnect.entity.abonnement.Paiement;
import com.agriconnect.agriconnect.entity.abonnement.PlanAbonnement;
import com.agriconnect.agriconnect.services.config.CentralConfig;
import com.agriconnect.agriconnect.services.dto.abonnement.response.AbonnementResponse;
import com.agriconnect.agriconnect.services.dto.abonnement.response.PaiementResponse;
import com.agriconnect.agriconnect.services.dto.abonnement.response.PlanResponse;
import org.mapstruct.Mapper;

import java.util.List;

// ===== AbonnementMapper.java =====
@Mapper(config = CentralConfig.class)
public interface AbonnementMapper {
    PlanResponse toDto(PlanAbonnement plan);
    List<PlanResponse> toPlanDtos(List<PlanAbonnement> plans);
    PaiementResponse toDto(Paiement paiement);
    AbonnementResponse toDto(Abonnement abonnement);        // plan et paiement via les méthodes ci-dessus
}