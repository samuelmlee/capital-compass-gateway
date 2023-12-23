package org.capitalcompass.capitalcompassgateway.exception;

public class UsersClientErrorException extends RuntimeException {
    private static final long serialVersionUID = 1545329359389210603L;

    public UsersClientErrorException(String s) {
        super(s);
    }
}
