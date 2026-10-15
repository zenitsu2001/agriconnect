package com.agriconnect.agriconnect.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
// ===== StatutPaiement.java =====
@Getter @RequiredArgsConstructor
public enum StatutPaiement implements FrontCode {
    EN_ATTENTE("pending"), REUSSI("success"), ECHOUE("failed"), REMBOURSE("refunded");
    private final String code;
}

