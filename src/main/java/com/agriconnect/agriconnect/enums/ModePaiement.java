package com.agriconnect.agriconnect.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
// ===== ModePaiement.java =====
@Getter @RequiredArgsConstructor
public enum ModePaiement implements FrontCode {
    WAVE("wave"), ORANGE_MONEY("orange"), CARTE("carte");
    private final String code;
}
