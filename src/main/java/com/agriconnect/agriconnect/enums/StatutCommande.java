package com.agriconnect.agriconnect.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
// ===== StatutCommande.java =====
@Getter @RequiredArgsConstructor
public enum StatutCommande implements FrontCode {
    EN_ATTENTE("pending"), CONFIRMEE("confirmed"), LIVREE("delivered"), ANNULEE("cancelled");
    private final String code;
}
