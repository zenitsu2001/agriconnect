package com.agriconnect.agriconnect.repository.parametre;

import com.agriconnect.agriconnect.entity.parametre.ParametreProfil;
import com.agriconnect.agriconnect.enums.Profil;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParametreProfilRepository extends JpaRepository<ParametreProfil, Profil> {}