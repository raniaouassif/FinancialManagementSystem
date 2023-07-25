package com.sg.FinancialManagementSystem.dto;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExchangeOrganization)) return false;
        ExchangeOrganization that = (ExchangeOrganization) o;
        return getExchangeOrganizationID() == that.getExchangeOrganizationID() && Objects.equals(getTickerCode(), that.getTickerCode()) && Objects.equals(getName(), that.getName()) && Objects.equals(getStocks(), that.getStocks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getExchangeOrganizationID(), getTickerCode(), getName(), getStocks());
    }
}
