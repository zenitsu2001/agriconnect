package com.agriconnect.agriconnect.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
// ===== StatutAbonnement.java =====
@Getter @RequiredArgsConstructor
public enum StatutAbonnement implements FrontCode {
    EN_ATTENTE("pending"), ACTIF("active"), EXPIRE("expired"), ANNULE("cancelled");
    private final String code;
}

