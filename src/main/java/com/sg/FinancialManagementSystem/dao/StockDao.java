package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.ExchangeOrganization;
import com.sg.FinancialManagementSystem.dto.Portfolio;
import com.sg.FinancialManagementSystem.dto.Stock;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
public interface StockDao {
    Stock getStockByID(int stockID);

    List<Stock> getAllStocks();

    Stock addStock(Stock stock);

    void updateStock(Stock stock);

    void deleteStockByID(int stockID);

    List<Stock> getStocksByExchangeOrganization(ExchangeOrganization exchangeOrganization);
    List<Stock> getStocksByPortfolio(Portfolio portfolio);

}
