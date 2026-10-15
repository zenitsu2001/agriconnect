package com.agriconnect.agriconnect.services.impl.auth;

// ===== AuthService.java =====
import com.agriconnect.agriconnect.entity.metier.Utilisateur;
import com.agriconnect.agriconnect.entity.parametre.LangueSupportee;
import com.agriconnect.agriconnect.entity.parametre.ParametreProfil;
import com.agriconnect.agriconnect.enums.*;
import com.agriconnect.agriconnect.exception.ApiException;
import com.agriconnect.agriconnect.services.dto.metier.Saisies;
import com.agriconnect.agriconnect.services.dto.metier.request.LoginRequest;
import com.agriconnect.agriconnect.services.dto.metier.request.RegisterRequest;
import com.agriconnect.agriconnect.services.dto.metier.response.AuthResponse;
import com.agriconnect.agriconnect.services.mapper.UtilisateurMapper;
import com.agriconnect.agriconnect.repository.metier.UtilisateurRepository;
import com.agriconnect.agriconnect.repository.parametre.LangueSupporteeRepository;
import com.agriconnect.agriconnect.repository.parametre.ParametreProfilRepository;
import com.agriconnect.agriconnect.security.JwtService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurs;
    private final LangueSupporteeRepository langues;
    private final ParametreProfilRepository parametresProfil;
    private final UtilisateurMapper mapper;
    private final PasswordEncoder encodeur;
    private final JwtService jwt;

    /** Hash factice : le temps de vérification reste le même si l'e-mail n'existe pas. */
    private String hashFactice;

    @PostConstruct
    void initialiser() {
        hashFactice = encodeur.encode("mot-de-passe-factice");
    }

    @Transactional
    public AuthResponse inscrire(RegisterRequest request) {
        if (!request.motDePasse().equals(request.confirmerMotDePasse())) {
            throw ApiException.badRequest("errors.register.passwordMismatch");
        }
        Profil profil = FrontCodes.fromCode(Profil.class, request.profil());   // code inconnu -> 400
        if (profil == Profil.ETAT) {                       // comptes de l'État créés uniquement par un administrateur
            throw ApiException.forbidden("errors.register.profileNotAllowed");
        }

        String email = Saisies.email(request.email());
        String telephone = Saisies.telephone(request.telephone());
        if (utilisateurs.existsByEmail(email)) throw ApiException.conflict("errors.register.emailTaken");
        if (utilisateurs.existsByTelephone(telephone)) throw ApiException.conflict("errors.register.phoneTaken");

        Utilisateur utilisateur = mapper.toEntity(request);
        utilisateur.setEmail(email);
        utilisateur.setTelephone(telephone);
        utilisateur.setMotDePasse(encodeur.encode(request.motDePasse()));
        utilisateur.setLangue(choisirLangue(request.langue(), profil));
        utilisateur.setActif(true);

        utilisateurs.saveAndFlush(utilisateur);            // une course sur l'unicité remonte ici (409)
        return reponse(utilisateur);
    }

    @Transactional
    public AuthResponse connecter(LoginRequest request) {
        Utilisateur utilisateur = utilisateurs.findByEmail(Saisies.email(request.email())).orElse(null);
        String hash = utilisateur != null ? utilisateur.getMotDePasse() : hashFactice;
        boolean motDePasseValide = encodeur.matches(request.motDePasse(), hash);

        // Même réponse quel que soit le motif (inconnu, mauvais mot de passe, compte désactivé)
        if (utilisateur == null || !motDePasseValide || !Boolean.TRUE.equals(utilisateur.getActif())) {
            throw ApiException.unauthorized("errors.auth.invalidCredentials");
        }
        utilisateur.setDerniereConnexion(LocalDateTime.now());
        return reponse(utilisateur);
    }

    /** Langue demandée si elle est active, sinon langue par défaut du profil (table parametres_profil). */
    private LangueSupportee choisirLangue(String code, Profil profil) {
        if (code != null && !code.isBlank()) {
            return langues.findByCodeAndActifTrue(code.trim().toLowerCase())
                    .orElseThrow(() -> ApiException.badRequest("errors.language.unsupported"));
        }
        return parametresProfil.findById(profil)
                .map(ParametreProfil::getLangueParDefaut)
                .orElseGet(() -> langues.findByParDefautTrue()
                        .orElseThrow(() -> new IllegalStateException("Aucune langue par défaut configurée")));
    }

    private AuthResponse reponse(Utilisateur utilisateur) {
        return new AuthResponse(jwt.generer(utilisateur), "Bearer", jwt.dureeEnSecondes(),
                mapper.toResponse(utilisateur));
    }
}