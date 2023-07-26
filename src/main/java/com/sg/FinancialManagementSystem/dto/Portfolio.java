package com.sg.FinancialManagementSystem.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
public class Portfolio {
    private int portfolioID;
    private BigDecimal balance;
    private BigDecimal bookValue;
    private BigDecimal marketValue;
    private BigDecimal totalReturn;
    private BigDecimal percentageReturn;
    private Customer customer;
    private List<Stock> stocks;
    private List<StockTransaction> stockTransactions;
    private List<PortfolioStock> portfolioStocks;

    public int getPortfolioID() {
        return portfolioID;
    }

    public void setPortfolioID(int portfolioID) {
        this.portfolioID = portfolioID;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBookValue() {
        return bookValue;
    }

    public void setBookValue(BigDecimal bookValue) {
        this.bookValue = bookValue;
    }

    public BigDecimal getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(BigDecimal marketValue) {
        this.marketValue = marketValue;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public List<StockTransaction> getStockTransactions() {
        return stockTransactions;
    }

    public void setStockTransactions(List<StockTransaction> stockTransactions) {
        this.stockTransactions = stockTransactions;
    }

    public List<PortfolioStock> getPortfolioStocks() {
        return portfolioStocks;
    }

    public void setPortfolioStocks(List<PortfolioStock> portfolioStocks) {
        this.portfolioStocks = portfolioStocks;
    }
}
