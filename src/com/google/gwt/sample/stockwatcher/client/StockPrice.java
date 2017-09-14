package com.google.gwt.sample.stockwatcher.client;

import java.io.Serializable;

/**
 * @author
 */
public class StockPrice implements Serializable {

    private String stock;

    private double price;

    private double change;

    public StockPrice() {
    }

    public StockPrice(String stock, double price, double change) {
        this.stock = stock;
        this.price = price;
        this.change = change;
    }

    public double calculateChangePercent() {
        return 100.0 * change / price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }
}
