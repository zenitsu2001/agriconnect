package com.agriconnect.agriconnect.services.impl.marche;

import com.agriconnect.agriconnect.services.dto.marche.FichierProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.time.Duration;

// ===== FichiersWebConfig.java =====  (sert les images enregistrées sous /fichiers/**)
@Configuration
@RequiredArgsConstructor
public class FichiersWebConfig implements WebMvcConfigurer {

    private final FichierProperties props;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String emplacement = Path.of(props.dossier()).toAbsolutePath().normalize().toUri().toString();
        if (!emplacement.endsWith("/")) emplacement += "/";
        registry.addResourceHandler("/fichiers/**")
                .addResourceLocations(emplacement)
                .setCacheControl(CacheControl.maxAge(Duration.ofDays(30)).cachePublic());   // les noms ne changent jamais
    }
}