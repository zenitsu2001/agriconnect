package com.agriconnect.agriconnect.security;
// ===== UtilisateurCourant.java =====  (utilisateur identifié par le jeton de la requête)
import com.agriconnect.agriconnect.entity.metier.Utilisateur;
import com.agriconnect.agriconnect.exception.ApiException;
import com.agriconnect.agriconnect.repository.metier.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UtilisateurCourant {

    private final UtilisateurRepository utilisateurs;

    public Long id() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) {
            throw ApiException.unauthorized("errors.auth.required");
        }
        return Long.valueOf(jwt.getSubject());
    }

    /** Recharge l'utilisateur et refuse un compte désactivé, même si son jeton est encore valide. */
    public Utilisateur entite() {
        return utilisateurs.findById(id())
                .filter(u -> Boolean.TRUE.equals(u.getActif()))
                .orElseThrow(() -> ApiException.unauthorized("errors.auth.accountDisabled"));
    }
}