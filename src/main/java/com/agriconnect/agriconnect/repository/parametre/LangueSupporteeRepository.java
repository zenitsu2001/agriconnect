package com.agriconnect.agriconnect.repository.parametre;

import com.agriconnect.agriconnect.entity.parametre.LangueSupportee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LangueSupporteeRepository extends JpaRepository<LangueSupportee, String> {
    List<LangueSupportee> findByActifTrueOrderByOrdreAsc();          // alimente le sélecteur de langue
    Optional<LangueSupportee> findByCodeAndActifTrue(String code);   // validation d'un choix
    Optional<LangueSupportee> findByParDefautTrue();
}