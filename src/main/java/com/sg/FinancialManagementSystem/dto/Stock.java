package com.sg.FinancialManagementSystem.dto;

import java.math.BigDecimal;

/**
 * @author raniaouassif on 2023-07-23
 */
public class Stock {
    private int stockID;
    private String tickerCode;
    private String name;
    private BigDecimal sharePrice;
    private int numberOfOutstandingShares;
    private BigDecimal marketCap;
    private int dailyVolume;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
