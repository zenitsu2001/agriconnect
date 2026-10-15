package com.agriconnect.agriconnect.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
// ===== TypeEvenement.java =====
@Getter @RequiredArgsConstructor
public enum TypeEvenement implements FrontCode {
    IRRIGATION("irrigation"), SEMIS("semis"), RECOLTE("harvest"), METEO("weather");
    private final String code;
}
