package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.i18n.client.Messages;

import java.util.Date;

/**
 * @author
 */
public interface StockWatcherMessages extends Messages{

    @DefaultMessage("''{0}'' is not a valid symbol")
    String invalidSymbol(String symbol);

    @DefaultMessage("Last update: {0,date,medium} {0,time,medium}")
    String lastUpdate(Date timeStamp);

    @DefaultMessage("Stock {0} was delisted")
    String delistedStock(String symbol);

    @DefaultMessage("Stock {0} is already on the list")
    String duplicateStock(String symbol);

    @DefaultMessage("Error: {0}")
    String error(String details);
}
