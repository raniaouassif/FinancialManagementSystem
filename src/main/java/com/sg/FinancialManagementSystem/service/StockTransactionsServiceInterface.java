package com.sg.FinancialManagementSystem.service;

import com.sg.FinancialManagementSystem.dto.Portfolio;
import com.sg.FinancialManagementSystem.dto.Stock;
import com.sg.FinancialManagementSystem.dto.StockTransaction;
import com.sg.FinancialManagementSystem.service.Exceptions.InsufficientFundsException;
import com.sg.FinancialManagementSystem.service.Exceptions.InsufficientStockSharesException;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-26
 */
public interface StockTransactionsServiceInterface {
    StockTransaction getStockTransactionByID(int stockTransactionID);
    List<StockTransaction> getAllStockTransactions();
    StockTransaction addStockTransaction(StockTransaction st) throws InsufficientFundsException, InsufficientStockSharesException;
    void updateStockTransaction(StockTransaction st);
    void deleteStockTransactionByID(int stockTransactionID);

    List<StockTransaction> getStockTransactionsByPortfolio(Portfolio portfolio);

    List<StockTransaction> getStockTransactionByStock(Stock stock);
}