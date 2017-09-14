package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

/**
 * @author
 */
@RemoteServiceRelativePath("prices")
public interface StockPriceService extends RemoteService {

    List<StockPrice> getPrices(List<String> stocks) throws DelistedException;
}
