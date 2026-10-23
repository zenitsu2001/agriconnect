package com.agriconnect.agriconnect.paiement;// ===== WaveGateway.java =====  (API Wave Business - Checkout)
// ⚠️ À CONFIRMER dans la documentation officielle (https://docs.wave.com/checkout) avant la mise en production :
//   - adresses et noms de champs de la création de session (/v1/checkout/sessions)
//   - valeurs de checkout_status / payment_status
//   - format de l'en-tête Wave-Signature et mode de calcul de la signature (ici : HMAC-SHA256 de « horodatage + corps »)
import com.agriconnect.agriconnect.entity.abonnement.Paiement;
import com.agriconnect.agriconnect.enums.ModePaiement;
import com.agriconnect.agriconnect.exception.paiement.PasserelleException;
import com.agriconnect.agriconnect.exception.paiement.SignatureInvalideException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class WaveGateway implements PasserellePaiement {

    private static final ParameterizedTypeReference<Map<String, Object>> JSON = new ParameterizedTypeReference<>() {};
    private static final long TOLERANCE_SECONDES = 300;            // rejet des notifications trop anciennes (rejeu)
    private static final Pattern REFERENCE = Pattern.compile("\"client_reference\"\\s*:\\s*\"([^\"]+)\"");

    private final RestClient http;
    private final String secretWebhook;

    public WaveGateway(PaiementProperties.Fournisseur config) {
        this.http = RestClient.builder()
                .baseUrl(config.baseUrl())
                .defaultHeader("Authorization", "Bearer " + config.cle())     // clé API : jamais côté Angular
                .build();
        this.secretWebhook = config.secretWebhook();
    }

    @Override public ModePaiement mode() { return ModePaiement.WAVE; }
    @Override public String fournisseur() { return "wave"; }

    @Override
    public SessionPaiement creerSession(DemandeSession demande) {
        Map<String, Object> corps = Map.of(
                // Le franc CFA n'a pas de centimes : montant entier, sous forme de texte
                "amount", demande.montant().setScale(0, RoundingMode.UNNECESSARY).toPlainString(),
                "currency", demande.devise(),
                "client_reference", demande.reference(),         // notre référence : Wave nous la renvoie
                "success_url", demande.urlSucces(),
                "error_url", demande.urlEchec());
        try {
            Map<String, Object> reponse = http.post().uri("/v1/checkout/sessions")
                    .contentType(MediaType.APPLICATION_JSON).body(corps)
                    .retrieve().body(JSON);
            if (reponse == null || reponse.get("id") == null || reponse.get("wave_launch_url") == null) {
                throw new PasserelleException("Réponse Wave incomplète");
            }
            return new SessionPaiement(String.valueOf(reponse.get("id")), String.valueOf(reponse.get("wave_launch_url")));
        } catch (RuntimeException e) {
            throw e instanceof PasserelleException ? e : new PasserelleException("Échec de la création de session Wave", e);
        }
    }

    @Override
    public StatutExterne verifier(Paiement paiement) {
        Map<String, Object> reponse;
        try {
            reponse = http.get().uri("/v1/checkout/sessions/{id}", paiement.getReferenceExterne())
                    .retrieve().body(JSON);
        } catch (RuntimeException e) {
            throw new PasserelleException("Échec de la vérification du paiement Wave", e);
        }
        if (reponse == null) throw new PasserelleException("Réponse Wave vide");

        String paiementStatut = String.valueOf(reponse.get("payment_status"));
        String sessionStatut = String.valueOf(reponse.get("checkout_status"));

        if ("succeeded".equals(paiementStatut)) {
            // Contrôles anti-fraude : la session doit bien correspondre à NOTRE paiement
            boolean referenceOk = paiement.getReference().equals(String.valueOf(reponse.get("client_reference")));
            boolean montantOk = montantConforme(reponse.get("amount"), paiement.getMontant());
            boolean deviseOk = paiement.getDevise().equals(String.valueOf(reponse.get("currency")));
            if (!(referenceOk && montantOk && deviseOk)) {
                // Ne JAMAIS confirmer : on laisse en attente pour examen manuel
                log.error("Paiement Wave incohérent, non confirmé : reference={} (référence ok={}, montant ok={}, devise ok={})",
                        paiement.getReference(), referenceOk, montantOk, deviseOk);
                return StatutExterne.EN_ATTENTE;
            }
            return StatutExterne.REUSSI;
        }
        if ("cancelled".equals(paiementStatut) || "expired".equals(sessionStatut)) return StatutExterne.ECHOUE;
        return StatutExterne.EN_ATTENTE;
    }

    @Override
    public Optional<NotificationPaiement> lireNotification(String corps, Map<String, String> entetes) {
        verifierSignature(corps, entetes.get("wave-signature"));

        // La notification ne fait que NOUS PRÉVENIR : on extrait notre référence, puis le statut est
        // redemandé à Wave par verifier(). (Simple expression régulière : aucune dépendance à une
        // bibliothèque JSON, et une référence erronée ne pourrait de toute façon rien confirmer.)
        Matcher m = REFERENCE.matcher(corps);
        return m.find() ? Optional.of(new NotificationPaiement(m.group(1))) : Optional.empty();
    }

    /** En-tête attendu : « Wave-Signature: t=<horodatage>,v1=<signature hexadécimale> ». */
    private void verifierSignature(String corps, String entete) {
        if (entete == null || secretWebhook == null || secretWebhook.isBlank()) throw new SignatureInvalideException();

        String horodatage = null;
        List<String> signatures = new ArrayList<>();
        for (String partie : entete.split(",")) {
            String[] cleValeur = partie.trim().split("=", 2);
            if (cleValeur.length != 2) continue;
            if (cleValeur[0].equals("t")) horodatage = cleValeur[1];
            else if (cleValeur[0].equals("v1")) signatures.add(cleValeur[1]);
        }
        if (horodatage == null || signatures.isEmpty()) throw new SignatureInvalideException();

        try {
            if (Math.abs(Instant.now().getEpochSecond() - Long.parseLong(horodatage)) > TOLERANCE_SECONDES) {
                throw new SignatureInvalideException();            // notification trop ancienne ou rejouée
            }
        } catch (NumberFormatException e) {
            throw new SignatureInvalideException();
        }

        String attendue = hmacSha256Hex(secretWebhook, horodatage + corps);
        // Comparaison en temps constant (évite de deviner la signature par mesure de temps)
        boolean valide = signatures.stream().anyMatch(s ->
                MessageDigest.isEqual(attendue.getBytes(StandardCharsets.UTF_8), s.getBytes(StandardCharsets.UTF_8)));
        if (!valide) throw new SignatureInvalideException();
    }

    private static boolean montantConforme(Object montantWave, BigDecimal montantAttendu) {
        try {
            return new BigDecimal(String.valueOf(montantWave)).compareTo(montantAttendu) == 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static String hmacSha256Hex(String secret, String donnees) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return HexFormat.of().formatHex(mac.doFinal(donnees.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException("Calcul de signature impossible", e);
        }
    }
}