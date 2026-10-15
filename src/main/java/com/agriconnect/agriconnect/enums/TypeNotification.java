package com.agriconnect.agriconnect.enums;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
// ===== TypeNotification.java =====
@Getter @RequiredArgsConstructor
public enum TypeNotification implements FrontCode {
    ALERTE_METEO("weatherAlert"), PUBLICATION("publication"), MESSAGE("message"),
    COMMANDE("order"), SYSTEME("system");
    private final String code;
}
