package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class StockWatcher implements EntryPoint {

    private static final int REFRESH_INTERVAL = 5000;

    private TextBox addStockTextBox = new TextBox();

    private FlexTable stockTable = new FlexTable();

    private Button addStockButton = new Button();

    private Label lastUpdateLabel = new Label();

    private Label errorMsgLabel = new Label();

    private VerticalPanel mainPanel = new VerticalPanel();

    private HorizontalPanel addStockPanel = new HorizontalPanel();

    private List<String> stocks = new ArrayList<>();

    private StockPriceServiceAsync stockPriceService = GWT.create(StockPriceService.class);

    private StockWatcherConstants constants = GWT.create(StockWatcherConstants.class);

    private StockWatcherMessages messages = GWT.create(StockWatcherMessages.class);

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        errorMsgLabel.setStyleName("errorMessage");
        errorMsgLabel.setVisible(false);

        stockTable.setText(0, 0, constants.symbol());
        stockTable.setText(0, 1, constants.price());
        stockTable.setText(0, 2, constants.change());
        stockTable.setText(0, 3, constants.remove());
        stockTable.setStyleName("watchList");

        addStockTextBox.getElement().setPropertyString("placeholder", constants.symbol());
        addStockTextBox.addKeyPressHandler(keyPressEvent -> {
            if (keyPressEvent.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                addStock();
            }
        });

        addStockButton.setText(constants.add());
        addStockButton.addClickHandler(clickEvent -> addStock());

        addStockPanel.add(addStockTextBox);
        addStockPanel.add(addStockButton);
        addStockPanel.setStyleName("addPanel");

        mainPanel.add(errorMsgLabel);
        mainPanel.add(stockTable);
        mainPanel.add(addStockPanel);
        mainPanel.add(lastUpdateLabel);

        RootPanel.get("stockList").add(mainPanel);
        RootPanel.get("appTitle").add(new Label(constants.stockWatcher()));

        Window.setTitle(constants.stockWatcher());

        addStockTextBox.setFocus(true);

        Timer timer = new Timer() {

            @Override
            public void run() {
                refreshWatchList();
            }
        };

        timer.scheduleRepeating(REFRESH_INTERVAL);
    }

    private void addStock() {
        String stock = addStockTextBox.getValue().toUpperCase().trim();

        if (stocks.contains(stock)) {
            Window.alert(messages.duplicateStock(stock));
            addStockTextBox.selectAll();
            return;
        }

        if (!stock.matches("^[0-9A-Z\\.]{1,10}$")) {
            Window.alert(messages.invalidSymbol(stock));
            addStockTextBox.selectAll();
            return;
        }

        stocks.add(stock);

        int row = stockTable.getRowCount();
        stockTable.setText(row, 0, stock);

        Button removeStockButton = new Button("x");
        removeStockButton.addClickHandler(clickEvent -> {
            int idx = stocks.indexOf(stock);
            stocks.remove(idx);
            stockTable.removeRow(idx + 1);
        });

        stockTable.setWidget(row, 3, removeStockButton);

        refreshWatchList();

        addStockTextBox.setValue(null);
        addStockTextBox.setFocus(true);
    }

    private void refreshWatchList() {
        stockPriceService.getPrices(stocks, new AsyncCallback<List<StockPrice>>() {

            @Override
            public void onFailure(Throwable throwable) {
                String details = throwable.getMessage();

                if (throwable instanceof DelistedException) {
                    details = messages.delistedStock(((DelistedException) throwable).getStock());
                }

                errorMsgLabel.setText(messages.error(details));
                errorMsgLabel.setVisible(true);
            }

            @Override
            public void onSuccess(List<StockPrice> stockPrices) {
                updateTable(stockPrices);
            }
        });
    }

    private void updateTable(List<StockPrice> stockPrices) {
        errorMsgLabel.setVisible(false);

        stockPrices.forEach(stockPrice -> {
            int row = stocks.indexOf(stockPrice.getStock()) + 1;

            stockTable.setText(row, 1, NumberFormat.getCurrencyFormat().format(stockPrice.getPrice()));
            NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");
            stockTable.setText(row, 2, changeFormat.format(stockPrice.getChange()) + "("
                    + changeFormat.format(stockPrice.calculateChangePercent()) + "%)");

            String changeStyle = "noChange";

            if (stockPrice.getChange() < -0.1) {
                changeStyle = "negativeChange";
            } else if (stockPrice.getChange() > 0.1) {
                changeStyle = "positiveChange";
            }

            stockTable.getCellFormatter().setStyleName(row, 2, changeStyle);

        });

        lastUpdateLabel.setText(messages.lastUpdate(new Date()));
    }
}
