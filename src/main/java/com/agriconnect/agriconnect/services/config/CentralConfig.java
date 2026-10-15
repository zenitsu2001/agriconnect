package com.agriconnect.agriconnect.services.config;

import org.mapstruct.*;

// ===== CentralConfig.java =====
@MapperConfig(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = CodeMapper.class,
        unmappedTargetPolicy = ReportingPolicy.WARN,              // passez à ERROR une fois les warnings traités
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,   // mises à jour partielles
        builder = @org.mapstruct.Builder(disableBuilder = true))  // nom complet : évite le conflit avec lombok.Builder
public interface CentralConfig {}