package com.agriconnect.agriconnect.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
// ===== TypeClient.java =====
@Getter @RequiredArgsConstructor
public enum TypeClient implements FrontCode {
    SUPERMARCHE("supermarket"), RESTAURATEUR("restaurant"), AUTRE("other");
    private final String code;
}

