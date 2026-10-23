package com.agriconnect.agriconnect.services.impl.marche;

import com.agriconnect.agriconnect.exception.ApiException;
import com.agriconnect.agriconnect.services.dto.marche.FichierProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class FichierService {

    /** Seule forme d'adresse acceptée pour une image : un UUID généré par NOTRE serveur. */
    private static final Pattern URL_STOCKEE =
            Pattern.compile("^/fichiers/([0-9a-f]{8}(?:-[0-9a-f]{4}){3}-[0-9a-f]{12}\\.(?:jpg|png))$");
    private static final int COTE_MAX = 1600;                 // pixels : on réduit les photos plus grandes
    private static final long PIXELS_MAX = 25_000_000L;       // refuse les images « bombes » (énormes une fois décompressées)

    private final Path dossier;
    private final long tailleMax;

    public FichierService(FichierProperties props) throws IOException {
        this.dossier = Path.of(props.dossier()).toAbsolutePath().normalize();
        Files.createDirectories(dossier);
        this.tailleMax = props.tailleMaxOctets();
    }

    /**
     * Enregistre une image envoyée par l'utilisateur et renvoie son adresse (« /fichiers/<uuid>.jpg »).
     * - le type est déterminé par le CONTENU du fichier (jamais par son nom ni son en-tête, falsifiables) ;
     * - seules les images JPEG et PNG sont acceptées ;
     * - l'image est ré-encodée : cela supprime les métadonnées (EXIF : position GPS, appareil...) ;
     * - le nom d'origine est ignoré : le fichier est renommé avec un UUID.
     */
    public String enregistrerImage(MultipartFile fichier) {
        if (fichier == null || fichier.isEmpty()) throw ApiException.badRequest("errors.file.invalid");
        if (fichier.getSize() > tailleMax) throw ApiException.badRequest("errors.file.tooLarge");

        try (ImageInputStream flux = ImageIO.createImageInputStream(fichier.getInputStream())) {
            if (flux == null) throw ApiException.badRequest("errors.file.invalid");
            Iterator<ImageReader> lecteurs = ImageIO.getImageReaders(flux);
            if (!lecteurs.hasNext()) throw ApiException.badRequest("errors.file.invalid");

            ImageReader lecteur = lecteurs.next();
            try {
                String format = lecteur.getFormatName().toLowerCase();
                if (!format.equals("jpeg") && !format.equals("png")) throw ApiException.badRequest("errors.file.invalid");

                lecteur.setInput(flux, true, true);
                // On contrôle les dimensions AVANT de décompresser l'image
                if ((long) lecteur.getWidth(0) * lecteur.getHeight(0) > PIXELS_MAX) {
                    throw ApiException.badRequest("errors.file.invalid");
                }
                return ecrire(reduire(lecteur.read(0)), format.equals("png"));
            } finally {
                lecteur.dispose();
            }
        } catch (IOException e) {
            throw ApiException.badRequest("errors.file.invalid");
        }
    }

    /** Vrai si l'adresse est celle d'une image que NOUS avons enregistrée et qui existe toujours. */
    public boolean estImageStockee(String url) {
        if (url == null) return false;
        var m = URL_STOCKEE.matcher(url);
        return m.matches() && Files.exists(dossier.resolve(m.group(1)));
    }

    // ------------------------------------------------------------------ détails

    private BufferedImage reduire(BufferedImage image) {
        int plusGrandCote = Math.max(image.getWidth(), image.getHeight());
        if (plusGrandCote <= COTE_MAX) return image;

        double ratio = (double) COTE_MAX / plusGrandCote;
        int largeur = Math.max(1, (int) Math.round(image.getWidth() * ratio));
        int hauteur = Math.max(1, (int) Math.round(image.getHeight() * ratio));
        BufferedImage reduite = new BufferedImage(largeur, hauteur,
                image.getColorModel().hasAlpha() ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
        Graphics2D g = reduite.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(image, 0, 0, largeur, hauteur, null);
        g.dispose();
        return reduite;
    }

    private String ecrire(BufferedImage image, boolean png) throws IOException {
        BufferedImage aEcrire = image;
        if (!png && image.getColorModel().hasAlpha()) {
            // Le JPEG ne gère pas la transparence : fond blanc
            aEcrire = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = aEcrire.createGraphics();
            g.drawImage(image, 0, 0, Color.WHITE, null);
            g.dispose();
        }
        String nom = UUID.randomUUID() + (png ? ".png" : ".jpg");
        if (!ImageIO.write(aEcrire, png ? "png" : "jpg", dossier.resolve(nom).toFile())) {
            throw new IOException("Écriture impossible");
        }
        return "/fichiers/" + nom;
    }
}