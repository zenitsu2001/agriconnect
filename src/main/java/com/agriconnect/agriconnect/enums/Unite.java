package com.agriconnect.agriconnect.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
// ===== Unite.java =====  (unité commune au marché, au stock et aux préventes)
// ===== Unite.java =====  (jeu unique : à adopter aussi côté Angular, à la place de L / units / bags)
@Getter @RequiredArgsConstructor
public enum Unite implements FrontCode {
    KG("kg"), TONNE("ton"), LITRE("liter"), UNITE("unit"), SAC("bag");
    private final String code;
}