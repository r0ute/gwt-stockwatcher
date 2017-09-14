package com.google.gwt.sample.stockwatcher.client;

/**
 * @author
 */
public class DelistedException extends Exception {

    private String stock;

    public DelistedException() {
    }

    public DelistedException(String stock) {
        this.stock = stock;
    }

    public String getStock() {
        return stock;
    }
}
