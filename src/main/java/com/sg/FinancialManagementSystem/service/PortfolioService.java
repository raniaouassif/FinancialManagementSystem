package com.sg.FinancialManagementSystem.service;

import com.sg.FinancialManagementSystem.dto.Customer;
import com.sg.FinancialManagementSystem.dto.Portfolio;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-28
 */
public interface PortfolioService {
    Portfolio getPortfolioByID(int portfolioID);

    List<Portfolio> getAllPortfolios();

    Portfolio addPortfolio(Portfolio portfolio);

    void updatePortfolio(Portfolio portfolio);

    void deletePortfolioByID(int portfolioID);

    Portfolio getPortfolioByCustomer(int customerID);
}
