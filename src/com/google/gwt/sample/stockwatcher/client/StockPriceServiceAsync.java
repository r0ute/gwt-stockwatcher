package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

/**
 * @author
 */
public interface StockPriceServiceAsync{

    void getPrices(List<String> stocks, AsyncCallback<List<StockPrice>> callback);
}
