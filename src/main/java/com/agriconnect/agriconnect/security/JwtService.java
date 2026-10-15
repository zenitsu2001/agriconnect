package com.agriconnect.agriconnect.security;
// ===== JwtService.java =====
import com.agriconnect.agriconnect.entity.metier.Utilisateur;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder encodeur;
    private final JwtProperties proprietes;

    public String generer(Utilisateur utilisateur) {
        Instant maintenant = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(proprietes.issuer())
                .issuedAt(maintenant)
                .expiresAt(maintenant.plus(proprietes.expiration()))
                .subject(String.valueOf(utilisateur.getId()))
                .claim("role", utilisateur.getProfil().name())
                .claim("email", utilisateur.getEmail())
                .build();
        JwsHeader entete = JwsHeader.with(MacAlgorithm.HS256).build();
        return encodeur.encode(JwtEncoderParameters.from(entete, claims)).getTokenValue();
    }

    public long dureeEnSecondes() {
        return proprietes.expiration().toSeconds();
    }
}
