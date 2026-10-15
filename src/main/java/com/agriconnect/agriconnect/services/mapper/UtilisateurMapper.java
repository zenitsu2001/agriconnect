package com.agriconnect.agriconnect.services.mapper;

import com.agriconnect.agriconnect.entity.metier.*;
import com.agriconnect.agriconnect.enums.FrontCodes;
import com.agriconnect.agriconnect.enums.Profil;
import com.agriconnect.agriconnect.enums.TypeClient;
import com.agriconnect.agriconnect.services.config.CentralConfig;
import com.agriconnect.agriconnect.services.config.ReferenceResolver;
import com.agriconnect.agriconnect.services.dto.metier.request.RegisterRequest;
import com.agriconnect.agriconnect.services.dto.metier.request.UpdateAgriculteurRequest;
import com.agriconnect.agriconnect.services.dto.metier.request.UpdateProfilRequest;
import com.agriconnect.agriconnect.services.dto.metier.response.ExploitationResponse;
import com.agriconnect.agriconnect.services.dto.metier.response.UtilisateurResponse;
import com.agriconnect.agriconnect.services.dto.metier.response.UtilisateurResume;
import org.hibernate.Hibernate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

// ===== UtilisateurMapper.java =====
@Mapper(config = CentralConfig.class, uses = {ReferenceMapper.class, ReferenceResolver.class})
public interface UtilisateurMapper {

    // --- Résumé : champs communs uniquement (fonctionne aussi sur un proxy LAZY) ---
    UtilisateurResume toResume(Utilisateur utilisateur);

    // --- Réponse complète : une méthode par sous-classe, aiguillage dans toResponse() ---
    @Mapping(source = "langue.code", target = "langue")
    @Mapping(source = "exploitation", target = "localisation")
    UtilisateurResponse fromAgriculteur(Agriculteur agriculteur);

    @Mapping(source = "langue.code", target = "langue")
    UtilisateurResponse fromClient(Client client);

    @Mapping(source = "langue.code", target = "langue")
    UtilisateurResponse fromInvestisseur(Investisseur investisseur);

    @Mapping(source = "langue.code", target = "langue")
    UtilisateurResponse fromPartenaire(Partenaire partenaire);

    @Mapping(source = "langue.code", target = "langue")
    UtilisateurResponse fromEtat(Etat etat);

    default UtilisateurResponse toResponse(Utilisateur utilisateur) {
        if (utilisateur == null) return null;
        // Un proxy Hibernate d'Utilisateur n'est pas une instance de la sous-classe : on le "déproxifie".
        Object reel = Hibernate.unproxy(utilisateur);
        if (reel instanceof Agriculteur a) return fromAgriculteur(a);
        if (reel instanceof Client c) return fromClient(c);
        if (reel instanceof Investisseur i) return fromInvestisseur(i);
        if (reel instanceof Partenaire p) return fromPartenaire(p);
        if (reel instanceof Etat e) return fromEtat(e);
        throw new IllegalStateException("Type d'utilisateur inconnu : " + reel.getClass().getName());
    }

    // --- Inscription : on crée la bonne sous-classe, MapStruct remplit les champs communs ---
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "motDePasse", ignore = true)          // haché par le service (BCrypt)
    @Mapping(target = "langue", ignore = true)              // choisie par le service (choix ou ParametreProfil)
    @Mapping(target = "actif", ignore = true)
    @Mapping(target = "derniereConnexion", ignore = true)
    @Mapping(target = "photoProfilUrl", ignore = true)
    void remplirBase(RegisterRequest request, @MappingTarget Utilisateur utilisateur);

    default Utilisateur toEntity(RegisterRequest request) {
        Profil profil = FrontCodes.fromCode(Profil.class, request.profil());
        Utilisateur utilisateur = switch (profil) {
            case AGRICULTEUR -> {
                Agriculteur a = new Agriculteur();
                a.setNomExploitation(request.nomExploitation());
                yield a;
            }
            case CLIENT -> {
                Client c = new Client();
                c.setNomEntreprise(request.nomEntreprise());
                c.setTypeClient(FrontCodes.fromCode(TypeClient.class, request.typeClient()));
                yield c;
            }
            case INVESTISSEUR -> {
                Investisseur i = new Investisseur();
                i.setOrganisation(request.organisation());
                yield i;
            }
            case PARTENAIRE -> {
                Partenaire p = new Partenaire();
                p.setOrganisation(request.organisation());
                yield p;
            }
            case ETAT -> {
                Etat e = new Etat();
                e.setInstitution(request.institution());
                yield e;
            }
        };
        remplirBase(request, utilisateur);
        return utilisateur;
    }

    // --- Mises à jour partielles (champs null ignorés) ---
    @Mapping(target = "langue", source = "langue")          // code -> LangueSupportee via ReferenceResolver
    void mettreAJour(UpdateProfilRequest request, @MappingTarget Utilisateur utilisateur);

//    // (la validation de la localisation exige une logique que MapStruct ne peut pas porter)
    @Mapping(source = "exploitation", target = "localisation")
    ExploitationResponse toExploitation(Agriculteur agriculteur);

//    @Mapping(source = "localisation", target = "exploitation")
//    void mettreAJour(UpdateAgriculteurRequest request, @MappingTarget Agriculteur agriculteur);
}