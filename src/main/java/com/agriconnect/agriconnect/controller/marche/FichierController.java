package com.agriconnect.agriconnect.controller.marche;

import com.agriconnect.agriconnect.services.dto.marche.response.FichierResponse;
import com.agriconnect.agriconnect.services.impl.marche.FichierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

// ===== FichierController.java =====
@RestController
@RequestMapping("/api/fichiers")
@RequiredArgsConstructor
public class FichierController {

    private final FichierService fichierService;

    /** POST /api/fichiers/images  (formulaire « multipart », champ « fichier »). Il faut être connecté. */
    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public FichierResponse televerser(@RequestParam("fichier") MultipartFile fichier) {
        return new FichierResponse(fichierService.enregistrerImage(fichier));
    }
}