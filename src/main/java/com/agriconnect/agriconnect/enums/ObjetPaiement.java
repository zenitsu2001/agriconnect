package com.agriconnect.agriconnect.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
// ===== ObjetPaiement.java =====
@Getter @RequiredArgsConstructor
public enum ObjetPaiement implements FrontCode {
    ABONNEMENT("subscription"), BOOST("boost");
    private final String code;
}