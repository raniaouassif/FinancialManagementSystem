package com.sg.FinancialManagementSystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockTransaction)) return false;
        StockTransaction that = (StockTransaction) o;
        return getStockTransactionID() == that.getStockTransactionID() && getNumberOfShares() == that.getNumberOfShares() && Objects.equals(getDateTime(), that.getDateTime()) && getType() == that.getType() && Objects.equals(getTransactionCost(), that.getTransactionCost()) && Objects.equals(getPortfolio(), that.getPortfolio()) && Objects.equals(getStock(), that.getStock()) && Objects.equals(getEo(), that.getEo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStockTransactionID(), getDateTime(), getType(), getNumberOfShares(), getTransactionCost(), getPortfolio(), getStock(), getEo());
    }
}
