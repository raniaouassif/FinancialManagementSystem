package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.Portfolio;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
public interface PortfolioDao {

    Portfolio getPortfolioByID(int portfolioID);

    List<Portfolio> getAllPortfolios();

    Portfolio addPortfolio(Portfolio portfolio);

    void updatePortfolio(Portfolio portfolio);

    void deletePortfolioByID(int portfolioID);

}
