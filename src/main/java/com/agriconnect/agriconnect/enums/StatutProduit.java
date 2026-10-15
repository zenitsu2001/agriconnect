package com.agriconnect.agriconnect.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
// ===== StatutProduit.java =====
@Getter @RequiredArgsConstructor
public enum StatutProduit implements FrontCode {
    EN_VENTE("forSale"), VENDU("sold"), RETIRE("withdrawn");
    private final String code;
}