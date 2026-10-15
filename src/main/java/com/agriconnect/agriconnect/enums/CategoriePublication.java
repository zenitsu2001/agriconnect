package com.agriconnect.agriconnect.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// ===== CategoriePublication.java =====
@Getter
@RequiredArgsConstructor
public enum CategoriePublication implements FrontCode {
    NORME("norm"), ACTUALITE("news"), ALERTE("alert");
    private final String code;
}
