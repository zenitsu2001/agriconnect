package com.agriconnect.agriconnect.repository.metier;

// ===== Utilisateurs =====

import com.agriconnect.agriconnect.entity.metier.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByTelephone(String telephone);
}