package com.agriconnect.agriconnect.paiement;// ===== PasserelleRegistre.java =====  (retrouve la passerelle d'un mode de paiement)
import com.agriconnect.agriconnect.enums.ModePaiement;
import com.agriconnect.agriconnect.exception.ApiException;
import java.util.List;
import java.util.Map;

public class PasserelleRegistre {

    private final Map<ModePaiement, PasserellePaiement> passerelles;

    public PasserelleRegistre(Map<ModePaiement, PasserellePaiement> passerelles) {
        this.passerelles = passerelles;
    }

    /** Mode non activé (ex. carte) -> erreur 400 traduisible. */
    public PasserellePaiement pour(ModePaiement mode) {
        PasserellePaiement passerelle = passerelles.get(mode);
        if (passerelle == null) throw ApiException.badRequest("errors.payment.modeUnavailable");
        return passerelle;
    }

    /** Modes proposés à l'utilisateur (dans l'ordre de l'enum). */
    public List<ModePaiement> modesActifs() {
        return List.copyOf(passerelles.keySet());
    }
}