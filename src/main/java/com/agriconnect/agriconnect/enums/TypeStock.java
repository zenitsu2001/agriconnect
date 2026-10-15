package com.agriconnect.agriconnect.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
// ===== TypeStock.java =====
@Getter @RequiredArgsConstructor
public enum TypeStock implements FrontCode {
    RECOLTE("harvest"), INTRANT("input"), SEMENCE("seed");
    private final String code;
}

