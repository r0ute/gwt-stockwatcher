package com.google.gwt.sample.stockwatcher.server;

import com.google.gwt.sample.stockwatcher.client.DelistedException;
import com.google.gwt.sample.stockwatcher.client.StockPrice;
import com.google.gwt.sample.stockwatcher.client.StockPriceService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author
 */
public class StockPriceServiceImpl extends RemoteServiceServlet implements StockPriceService {

    public static final double MAX_PRICE = 100.0;
    public static final double MAX_PRICE_CHANGE = 0.02;

    @Override
    public List<StockPrice> getPrices(List<String> stocks) throws DelistedException {
        if (stocks.stream().anyMatch("ERR"::equals)) {
            throw new DelistedException("ERR");
        }

        return stocks.stream()
                .map(s -> {
                    double price = new Random().nextDouble() * MAX_PRICE;
                    double change = price * MAX_PRICE_CHANGE * (new Random().nextDouble() * 2.0 - 1.0);

                    return new StockPrice(s, price, change);
                }).collect(Collectors.toList());
    }
}
