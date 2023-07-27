package com.sg.FinancialManagementSystem.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @author raniaouassif on 2023-07-23
 */
public class Stock {
    private int stockID;
    private String tickerCode;
    private StockStatus status;
    private BigDecimal sharePrice;
    private Long numberOfOutstandingShares;
    private BigDecimal marketCap;
    private Long dailyVolume;
    private Company company;
    private List<ExchangeOrganization> exchangeOrganizations;


    private String formattedMarketCap;
    private String formattedOutstandingShares;
    private String formattedDailyVolume;

    public int getStockID() {
        return stockID;
    }

    public void setStockID(int stockID) {
        this.stockID = stockID;
    }

    public String getTickerCode() {
        return tickerCode;
    }

    public void setTickerCode(String tickerCode) {
        this.tickerCode = tickerCode;
    }

    public StockStatus getStatus() {
        return status;
    }

    public void setStatus(StockStatus status) {
        this.status = status;
    }

    public BigDecimal getSharePrice() {
        return sharePrice;
    }

    public void setSharePrice(BigDecimal sharePrice) {
        this.sharePrice = sharePrice;
    }

    public Long getNumberOfOutstandingShares() {
        return numberOfOutstandingShares;
    }

    public void setNumberOfOutstandingShares(Long numberOfOutstandingShares) {
        this.numberOfOutstandingShares = numberOfOutstandingShares;
    }

    public BigDecimal getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(BigDecimal marketCap) {
        this.marketCap = marketCap;
    }

    public Long getDailyVolume() {
        return dailyVolume;
    }

    public void setDailyVolume(Long dailyVolume) {
        this.dailyVolume = dailyVolume;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<ExchangeOrganization> getExchangeOrganizations() {
        return exchangeOrganizations;
    }

    public void setExchangeOrganizations(List<ExchangeOrganization> exchangeOrganizations) {
        this.exchangeOrganizations = exchangeOrganizations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stock)) return false;
        Stock stock = (Stock) o;
        return getStockID() == stock.getStockID() && Objects.equals(getTickerCode(), stock.getTickerCode()) && getStatus() == stock.getStatus() && Objects.equals(getSharePrice(), stock.getSharePrice()) && Objects.equals(getNumberOfOutstandingShares(), stock.getNumberOfOutstandingShares()) && Objects.equals(getMarketCap(), stock.getMarketCap()) && Objects.equals(getDailyVolume(), stock.getDailyVolume()) && Objects.equals(getCompany(), stock.getCompany()) && Objects.equals(getExchangeOrganizations(), stock.getExchangeOrganizations());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStockID(), getTickerCode(), getStatus(), getSharePrice(), getNumberOfOutstandingShares(), getMarketCap(), getDailyVolume(), getCompany(), getExchangeOrganizations());
    }


    //NOT INCLUDED IN THE EQUAL & HASH

    public String getFormattedMarketCap() {
        return formattedMarketCap;
    }

    public void setFormattedMarketCap(String formattedMarketCap) {
        this.formattedMarketCap = formattedMarketCap;
    }

    public String getFormattedOutstandingShares() {
        return formattedOutstandingShares;
    }

    public void setFormattedOutstandingShares(String formattedOutstandingShares) {
        this.formattedOutstandingShares = formattedOutstandingShares;
    }

    public String getFormattedDailyVolume() {
        return formattedDailyVolume;
    }

    public void setFormattedDailyVolume(String formattedDailyVolume) {
        this.formattedDailyVolume = formattedDailyVolume;
    }
}

