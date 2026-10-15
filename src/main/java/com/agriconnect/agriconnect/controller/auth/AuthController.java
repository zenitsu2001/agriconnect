package com.agriconnect.agriconnect.controller.auth;

import com.agriconnect.agriconnect.services.impl.auth.AuthService;
import com.agriconnect.agriconnect.services.dto.metier.request.LoginRequest;
import com.agriconnect.agriconnect.services.dto.metier.request.RegisterRequest;
import com.agriconnect.agriconnect.services.dto.metier.response.AuthResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse inscrire(@Valid @RequestBody RegisterRequest request) {
        return authService.inscrire(request);               // renvoie aussi le jeton : l'étape d'abonnement suit l'inscription
    }

    @PostMapping("/login")
    public AuthResponse connecter(@Valid @RequestBody LoginRequest request) {
        return authService.connecter(request);
    }
}