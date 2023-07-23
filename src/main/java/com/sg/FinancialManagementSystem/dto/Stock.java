package com.sg.FinancialManagementSystem.dto;

import java.math.BigDecimal;

/**
 * @author raniaouassif on 2023-07-23
 */
public class Stock {
    private int stockID;
    private String tickerCode;
    private BigDecimal sharePrice;
    private int numberOfOutstandingShares;
    private BigDecimal marketCap;
    private int dailyVolume;
    private Company company;

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

    public BigDecimal getSharePrice() {
        return sharePrice;
    }

    public void setSharePrice(BigDecimal sharePrice) {
        this.sharePrice = sharePrice;
    }

    public int getNumberOfOutstandingShares() {
        return numberOfOutstandingShares;
    }

    public void setNumberOfOutstandingShares(int numberOfOutstandingShares) {
        this.numberOfOutstandingShares = numberOfOutstandingShares;
    }

    public BigDecimal getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(BigDecimal marketCap) {
        this.marketCap = marketCap;
    }

    public int getDailyVolume() {
        return dailyVolume;
    }

    public void setDailyVolume(int dailyVolume) {
        this.dailyVolume = dailyVolume;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
