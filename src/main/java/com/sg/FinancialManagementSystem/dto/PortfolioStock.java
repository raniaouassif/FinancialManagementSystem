package com.sg.FinancialManagementSystem.dto;

import javax.sound.sampled.Port;
import java.math.BigDecimal;

/**
 * @author raniaouassif on 2023-07-25
 */
public class PortfolioStock {
    private int portfolioStockID;
    private int numberOfShares;
    private BigDecimal marketValue;
    private BigDecimal bookValue;
    private BigDecimal averagePrice;
    private BigDecimal totalReturn;
    private BigDecimal percentageReturn;
    private Portfolio portfolio;
    private Stock stock;

    public int getPortfolioStockID() {
        return portfolioStockID;
    }

    public void setPortfolioStockID(int portfolioStockID) {
        this.portfolioStockID = portfolioStockID;
    }

    public int getNumberOfShares() {
        return numberOfShares;
    }

    public void setNumberOfShares(int numberOfShares) {
        this.numberOfShares = numberOfShares;
    }

    public BigDecimal getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(BigDecimal marketValue) {
        this.marketValue = marketValue;
    }

    public BigDecimal getBookValue() {
        return bookValue;
    }

    public void setBookValue(BigDecimal bookValue) {
        this.bookValue = bookValue;
    }

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
    }

    public BigDecimal getTotalReturn() {
        return totalReturn;
    }

    public void setTotalReturn(BigDecimal totalReturn) {
        this.totalReturn = totalReturn;
    }

    public BigDecimal getPercentageReturn() {
        return percentageReturn;
    }

    public void setPercentageReturn(BigDecimal percentageReturn) {
        this.percentageReturn = percentageReturn;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
}
