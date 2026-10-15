package com.agriconnect.agriconnect.enums;

import com.agriconnect.agriconnect.exception.CodeInconnuException;

// ===== FrontCodes.java =====
public final class FrontCodes {
    private FrontCodes() {}

    public static <E extends Enum<E> & FrontCode> String toCode(E value) {
        return value == null ? null : value.getCode();
    }

    /** Code inconnu -> IllegalArgumentException (à traduire en HTTP 400 dans le @RestControllerAdvice). */
    public static <E extends Enum<E> & FrontCode> E fromCode(Class<E> type, String code) {
        if (code == null) return null;
        for (E value : type.getEnumConstants()) {
            if (value.getCode().equalsIgnoreCase(code)) return value;
        }
        throw new CodeInconnuException(type.getSimpleName(), code);
    }
}