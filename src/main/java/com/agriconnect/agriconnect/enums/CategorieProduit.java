package com.agriconnect.agriconnect.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
// ===== CategorieProduit.java =====
@Getter @RequiredArgsConstructor
public enum CategorieProduit implements FrontCode {
    LEGUMES("vegetables"), FRUITS("fruits"), CEREALES("cereals"), AUTRES("other");
    private final String code;
}
