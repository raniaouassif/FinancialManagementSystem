package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
public interface StockPortfolioTransactionDao {
    StockPortfolioTransaction getStockPortfolioTransactionByID(int stockPortfolioTransactionID);

    List<StockPortfolioTransaction> getAllStockPortfolioTransactions();

    StockPortfolioTransaction addStockPortfolioTransaction(StockPortfolioTransaction stockPortfolioTransaction);

    void updateStockPortfolioTransaction(StockPortfolioTransaction stockPortfolioTransaction);

    void deleteStockPortfolioTransactionByID(int stockPortfolioTransactionID);

    List<StockPortfolioTransaction> getStockPortfolioTransactionsByStock(Stock stock);

    List<StockPortfolioTransaction> getStockPortfolioTransactionsByPortfolio(Portfolio portfolio);

    List<StockPortfolioTransaction> getStockPortfolioTransactionsByType(StockTransactionType type);

    List<StockPortfolioTransaction> getStockPortfolioTransactionsByDate(LocalDate date);

    List<StockPortfolioTransaction> getStockPortfolioTransactionsByExchangeOrganization(ExchangeOrganization organization);

}
