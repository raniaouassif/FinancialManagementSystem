package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.Portfolio;
import com.sg.FinancialManagementSystem.dto.PortfolioStock;
import com.sg.FinancialManagementSystem.dto.Stock;
import com.sg.FinancialManagementSystem.dto.StockTransaction;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-25
 */
public interface StockTransactionDao {
    StockTransaction getStockTransactionByID(int stockTransactionID);
    List<StockTransaction> getAllStockTransactions();
    StockTransaction addStockTransaction(StockTransaction st);
    void updateStockTransaction(StockTransaction st);
    void deleteStockTransactionByID(int stockTransactionID);

    List<StockTransaction> getStockTransactionsByPortfolio(Portfolio portfolio);

    List<StockTransaction> getStockTransactionByStock(Stock stock);
}
