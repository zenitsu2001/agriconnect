package com.agriconnect.agriconnect.exception;// ===== CodeInconnuException.java =====  (code d'enum reçu du frontend non reconnu)
import lombok.Getter;

@Getter
public class CodeInconnuException extends IllegalArgumentException {
    private final String type;
    private final String code;

    public CodeInconnuException(String type, String code) {
        super("Code inconnu pour " + type + " : " + code);
        this.type = type;
        this.code = code;
    }
}