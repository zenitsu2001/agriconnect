package com.agriconnect.agriconnect.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
// ===== StatutStock.java =====  (bas / normal / bon dans l'interface)
@Getter @RequiredArgsConstructor
public enum StatutStock implements FrontCode {
    BAS("low"), NORMAL("normal"), BON("high");
    private final String code;
}
