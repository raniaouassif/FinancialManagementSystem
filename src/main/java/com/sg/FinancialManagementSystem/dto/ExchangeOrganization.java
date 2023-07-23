package com.sg.FinancialManagementSystem.dto;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
public class ExchangeOrganization {
    private int exchangeOrganizationID;
    private String tickerCode;
    private String name;
    private List<Stock> stocks;

    public int getExchangeOrganizationID() {
        return exchangeOrganizationID;
    }

    public void setExchangeOrganizationID(int exchangeOrganizationID) {
        this.exchangeOrganizationID = exchangeOrganizationID;
    }

    public String getTickerCode() {
        return tickerCode;
    }

    public void setTickerCode(String tickerCode) {
        this.tickerCode = tickerCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }
}
