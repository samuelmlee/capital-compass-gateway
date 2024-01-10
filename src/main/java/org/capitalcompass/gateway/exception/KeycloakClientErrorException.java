package org.capitalcompass.gateway.exception;

public class KeycloakClientErrorException extends RuntimeException {
    public KeycloakClientErrorException(String s) {
        super(s);
    }
}
