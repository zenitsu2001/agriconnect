package com.agriconnect.agriconnect.services.config;// ===== CodeMapper.java =====  (enum <-> code Angular : une seule source de vérité, dans les enums)
import com.agriconnect.agriconnect.enums.*;
import org.springframework.stereotype.Component;

@Component
public class CodeMapper {
    public String toCode(Profil v) { return FrontCodes.toCode(v); }
    public Profil toProfil(String c) { return FrontCodes.fromCode(Profil.class, c); }

    public String toCode(TypeClient v) { return FrontCodes.toCode(v); }
    public TypeClient toTypeClient(String c) { return FrontCodes.fromCode(TypeClient.class, c); }

    public String toCode(CategorieProduit v) { return FrontCodes.toCode(v); }
    public CategorieProduit toCategorieProduit(String c) { return FrontCodes.fromCode(CategorieProduit.class, c); }

    public String toCode(Unite v) { return FrontCodes.toCode(v); }
    public Unite toUnite(String c) { return FrontCodes.fromCode(Unite.class, c); }

    public String toCode(StatutProduit v) { return FrontCodes.toCode(v); }
    public StatutProduit toStatutProduit(String c) { return FrontCodes.fromCode(StatutProduit.class, c); }

    public String toCode(StatutCommande v) { return FrontCodes.toCode(v); }
    public StatutCommande toStatutCommande(String c) { return FrontCodes.fromCode(StatutCommande.class, c); }

    public String toCode(TypeStock v) { return FrontCodes.toCode(v); }
    public TypeStock toTypeStock(String c) { return FrontCodes.fromCode(TypeStock.class, c); }

    public String toCode(StatutStock v) { return FrontCodes.toCode(v); }
    public StatutStock toStatutStock(String c) { return FrontCodes.fromCode(StatutStock.class, c); }

    public String toCode(TypeMouvement v) { return FrontCodes.toCode(v); }
    public TypeMouvement toTypeMouvement(String c) { return FrontCodes.fromCode(TypeMouvement.class, c); }

    public String toCode(TypeEvenement v) { return FrontCodes.toCode(v); }
    public TypeEvenement toTypeEvenement(String c) { return FrontCodes.fromCode(TypeEvenement.class, c); }

    public String toCode(Priorite v) { return FrontCodes.toCode(v); }
    public Priorite toPriorite(String c) { return FrontCodes.fromCode(Priorite.class, c); }

    public String toCode(CategoriePublication v) { return FrontCodes.toCode(v); }
    public CategoriePublication toCategoriePublication(String c) { return FrontCodes.fromCode(CategoriePublication.class, c); }

    public String toCode(ModePaiement v) { return FrontCodes.toCode(v); }
    public ModePaiement toModePaiement(String c) { return FrontCodes.fromCode(ModePaiement.class, c); }

    public String toCode(StatutPaiement v) { return FrontCodes.toCode(v); }
    public StatutPaiement toStatutPaiement(String c) { return FrontCodes.fromCode(StatutPaiement.class, c); }

    public String toCode(ObjetPaiement v) { return FrontCodes.toCode(v); }
    public ObjetPaiement toObjetPaiement(String c) { return FrontCodes.fromCode(ObjetPaiement.class, c); }

    public String toCode(PeriodeAbonnement v) { return FrontCodes.toCode(v); }
    public PeriodeAbonnement toPeriodeAbonnement(String c) { return FrontCodes.fromCode(PeriodeAbonnement.class, c); }

    public String toCode(StatutAbonnement v) { return FrontCodes.toCode(v); }
    public StatutAbonnement toStatutAbonnement(String c) { return FrontCodes.fromCode(StatutAbonnement.class, c); }

    public String toCode(TypeNotification v) { return FrontCodes.toCode(v); }
    public TypeNotification toTypeNotification(String c) { return FrontCodes.fromCode(TypeNotification.class, c); }
}