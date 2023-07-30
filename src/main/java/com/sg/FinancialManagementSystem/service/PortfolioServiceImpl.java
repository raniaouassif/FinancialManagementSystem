package com.sg.FinancialManagementSystem.service;

import com.sg.FinancialManagementSystem.dao.PortfolioDao;
import com.sg.FinancialManagementSystem.dto.Customer;
import com.sg.FinancialManagementSystem.dto.Portfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-28
 */

@Service
public class PortfolioServiceImpl implements  PortfolioService{

    @Autowired
    PortfolioDao portfolioDao;
    @Override
    public Portfolio getPortfolioByID(int portfolioID) {
        return portfolioDao.getPortfolioByID(portfolioID);
    }

    @Override
    public List<Portfolio> getAllPortfolios() {
        return portfolioDao.getAllPortfolios();
    }

    @Override
    public Portfolio addPortfolio(Portfolio portfolio) {
        return null;
    }

    @Override
    public void updatePortfolio(Portfolio portfolio) {

    }

    @Override
    public void deletePortfolioByID(int portfolioID) {

    }

    @Override
    public Portfolio getPortfolioByCustomer(int customerID) {
        return portfolioDao.getPortfolioByCustomer(customerID);
    }
}
