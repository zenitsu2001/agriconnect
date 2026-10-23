package com.agriconnect.agriconnect.services.dto.marche;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({MarcheProperties.class, FichierProperties.class})
public class MarcheConfig {}