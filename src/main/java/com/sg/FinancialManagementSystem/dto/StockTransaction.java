package com.sg.FinancialManagementSystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author raniaouassif on 2023-07-25
 */
public class StockTransaction {
    private int stockTransactionID;

    private LocalDateTime dateTime;
    private StockTransactionType type;
    private int numberOfShares;
    private BigDecimal transactionCost;

    private Portfolio portfolio;
    private Stock stock;
    private ExchangeOrganization eo;


    public int getStockTransactionID() {
        return stockTransactionID;
    }

    public void setStockTransactionID(int stockTransactionID) {
        this.stockTransactionID = stockTransactionID;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public StockTransactionType getType() {
        return type;
    }

    public void setType(StockTransactionType type) {
        this.type = type;
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

    public ExchangeOrganization getEo() {
        return eo;
    }

    public void setEo(ExchangeOrganization eo) {
        this.eo = eo;
    }
}
