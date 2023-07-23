package com.sg.FinancialManagementSystem.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
public class Portfolio {
    private int portfolioID;
    private BigDecimal balance;
    private List<Stock> stocks;

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

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }
}
