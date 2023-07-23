package com.sg.FinancialManagementSystem.dto;

import java.math.BigDecimal;

/**
 * @author raniaouassif on 2023-07-23
 */
public class Stock {
    private int stockID;
    private String tickerCode;
    private BigDecimal sharePrice;
    private Long numberOfOutstandingShares;
    private BigDecimal marketCap;
    private Long dailyVolume;
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
}
