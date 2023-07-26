package com.sg.FinancialManagementSystem.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Portfolio)) return false;
        Portfolio portfolio = (Portfolio) o;
        return getPortfolioID() == portfolio.getPortfolioID() && Objects.equals(getBalance(), portfolio.getBalance()) && Objects.equals(getBookValue(), portfolio.getBookValue()) && Objects.equals(getMarketValue(), portfolio.getMarketValue()) && Objects.equals(getTotalReturn(), portfolio.getTotalReturn()) && Objects.equals(getPercentageReturn(), portfolio.getPercentageReturn()) && Objects.equals(getCustomer(), portfolio.getCustomer()) && Objects.equals(getStockTransactions(), portfolio.getStockTransactions()) && Objects.equals(getPortfolioStocks(), portfolio.getPortfolioStocks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPortfolioID(), getBalance(), getBookValue(), getMarketValue(), getTotalReturn(), getPercentageReturn(), getCustomer(), getStockTransactions(), getPortfolioStocks());
    }
}
