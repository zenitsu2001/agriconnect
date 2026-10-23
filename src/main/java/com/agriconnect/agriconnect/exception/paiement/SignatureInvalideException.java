package com.agriconnect.agriconnect.exception.paiement;

public class SignatureInvalideException extends RuntimeException {
    public SignatureInvalideException() { super("Signature de notification invalide"); }
}