package org.capitalcompass.capitalcompassgateway.exception;

public class StocksClientErrorException extends RuntimeException {
    private static final long serialVersionUID = -93668842896125683L;

    public StocksClientErrorException(String s) {
        super(s);
    }
}
