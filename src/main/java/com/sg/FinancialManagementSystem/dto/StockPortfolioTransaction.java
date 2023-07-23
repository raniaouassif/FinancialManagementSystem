package com.sg.FinancialManagementSystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author raniaouassif on 2023-07-23
 */
public class StockPortfolioTransaction {
    private int stockPortfolioTransactionID;
    private LocalDateTime dateTime;
    private int numberOfShares;
    private BigDecimal transactionCost;

    public int getStockPortfolioTransactionID() {
        return stockPortfolioTransactionID;
    }

    public void setStockPortfolioTransactionID(int stockPortfolioTransactionID) {
        this.stockPortfolioTransactionID = stockPortfolioTransactionID;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getNumberOfShares() {
        return numberOfShares;
    }

    public void setNumberOfShares(int numberOfShares) {
        this.numberOfShares = numberOfShares;
    }

    public BigDecimal getTransactionCost() {
        return transactionCost;
    }

    public void setTransactionCost(BigDecimal transactionCost) {
        this.transactionCost = transactionCost;
    }
}
