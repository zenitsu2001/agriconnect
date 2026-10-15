package com.agriconnect.agriconnect.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
// ===== Priorite.java =====
@Getter @RequiredArgsConstructor
public enum Priorite implements FrontCode {
    HAUTE("high"), MOYENNE("medium"), BASSE("low");
    private final String code;
}
