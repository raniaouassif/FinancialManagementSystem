package com.sg.FinancialManagementSystem.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

/**
 * @author raniaouassif on 2023-07-23
 */
public class ExchangeOrganization {
    private int exchangeOrganizationID;
    @NotBlank(message = "Ticker must not be blank")
    @Size(max = 10, message = "Ticker must be fewer than 10 characters")
    @Pattern(regexp = "^[A-Z]+$", message = "Ticker must contain only capital letters.")
    private String tickerCode;
    @NotBlank(message = "Name must not be blank")
    @Size(max = 50, message = "Name must be fewer than 50 characters")
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
