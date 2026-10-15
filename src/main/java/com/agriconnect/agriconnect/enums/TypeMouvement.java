package com.agriconnect.agriconnect.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
// ===== TypeMouvement.java =====
@Getter @RequiredArgsConstructor
public enum TypeMouvement implements FrontCode {
    ENTREE("entry"), SORTIE("exit");
    private final String code;
}
