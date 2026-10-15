package com.agriconnect.agriconnect.services.dto.metier.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank @Email String email, @NotBlank String motDePasse) {}
