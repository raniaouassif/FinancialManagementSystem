package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.Portfolio;
import com.sg.FinancialManagementSystem.dto.PortfolioStock;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-25
 */
public interface PortfolioStockDao {
    PortfolioStock getPortfolioStockByID(int portfolioStockID);
    List<PortfolioStock> getAllPortfolioStocks();
    PortfolioStock addPortfolioStock(PortfolioStock portfolioStock);
    void updatePortfolioStock(PortfolioStock portfolioStock);
    void deletePortfolioStockByID(int portfolioStockID);
    List<PortfolioStock> getPortfolioStocksByPortfolio(Portfolio portfolio);
}