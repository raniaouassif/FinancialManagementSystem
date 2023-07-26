package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dao.mappers.*;
import com.sg.FinancialManagementSystem.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-25
 */
@Repository
public class PortfolioDaoDB implements PortfolioDao{
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public Portfolio getPortfolioByID(int portfolioID) {
        try {
            final String GET_PORTFOLIO_BY_ID = "SELECT * FROM Portfolio WHERE portfolioID = ?";
            Portfolio portfolio = jdbcTemplate.queryForObject(GET_PORTFOLIO_BY_ID, new PortfolioMapper(), portfolioID);

            //Set the portfolio Customer, transactions, portfoliostocks
            set_Customer_Transactions_Stocks_By_Portfolio(portfolio);

            return portfolio;
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Portfolio> getAllPortfolios() {
        final String GET_ALL_PORTFOLIOS = "SELECT * FROM Portfolio";
        List<Portfolio> portfolios = jdbcTemplate.query(GET_ALL_PORTFOLIOS, new PortfolioMapper());
        //Set the portfolio Customer, stocks, transactions
        set_Customer_Transactions_Stocks_By_PortfoliosList(portfolios);
        return portfolios;
    }

    @Override
    public Portfolio addPortfolio(Portfolio portfolio) {
        final String ADD_PORTFOLIO = "INSERT INTO Portfolio " +
                "(balance, bookValue, marketValue, totalReturn, percentageReturn, customerID) " +
                "VALUES (?,?,?,?,?,?)";

        jdbcTemplate.update(ADD_PORTFOLIO,
                0.00, // NO BALANCE AT FIRST
                0.00,
                0.00,
                0.00,
                0.00,
                portfolio.getCustomer().getCustomerID()
        );

        //Retrieve the new id
        int newID = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        portfolio.setPortfolioID(newID);

        // set the fields in the object too
        portfolio.setBalance(new BigDecimal(0.00).setScale(2));
        portfolio.setBookValue(new BigDecimal(0.00).setScale(2));
        portfolio.setMarketValue(new BigDecimal(0.00).setScale(2));
        portfolio.setTotalReturn(new BigDecimal(0.00).setScale(2));
        portfolio.setPercentageReturn(new BigDecimal(0.00).setScale(2));

        return portfolio;
    }

    @Override
    public void updatePortfolio(Portfolio portfolio) {
        final String UPDATE_PORTFOLIO = "UPDATE Portfolio SET " +
                "balance = ?, bookValue = ?, marketValue = ?, totalReturn = ?, percentageReturn = ?, customerID=? " +
                "WHERE portfolioID = ?";

        jdbcTemplate.update(UPDATE_PORTFOLIO,
                portfolio.getBalance(),
                portfolio.getBookValue(),
                portfolio.getMarketValue(),
                portfolio.getTotalReturn(),
                portfolio.getPercentageReturn(),
                portfolio.getCustomer().getCustomerID(),
                portfolio.getPortfolioID());
    }

    @Override
    public void deletePortfolioByID(int portfolioID) {
        // First delete the PortfolioBridge
        final String DELETE_PORTFOLIO_BRIDGE = "DELETE FROM PortfolioBridge WHERE portfolioID = ?";
        jdbcTemplate.update(DELETE_PORTFOLIO_BRIDGE, portfolioID);

        // Then delete the PortfolioStock details
        final String DELETE_PORTFOLIO_STOCK = "DELETE FROM PortfolioStock WHERE portfolioID = ?";
        jdbcTemplate.update(DELETE_PORTFOLIO_STOCK, portfolioID);

        //Then delete the portfolio
        final String DELETE_PORTFOLIO = "DELETE FROM Portfolio WHERE portfolioID = ?";
        jdbcTemplate.update(DELETE_PORTFOLIO, portfolioID);
    }

    //PRIVATE HELPER FUNCTIONS
    private void set_Customer_Transactions_Stocks_By_PortfoliosList(List<Portfolio> portfoliosList) {
        for (Portfolio portfolio : portfoliosList) {
            set_Customer_Transactions_Stocks_By_Portfolio(portfolio);
        }
    }
    private void set_Customer_Transactions_Stocks_By_Portfolio(Portfolio portfolio) {
        int portfolioID = portfolio.getPortfolioID();
        //Set the customer
        portfolio.setCustomer(getCustomerByPortfolio(portfolioID));

        //Set the stock transactions
        portfolio.setStockTransactions(getStockTransactionsByPortfolio(portfolioID));

        //Set the portfolio stocks
        portfolio.setPortfolioStocks(getPortfolioStocksByPortfolio(portfolioID));
    }

    private List<StockTransaction> getStockTransactionsByPortfolio(int portfolioID) {
        final String GET_TRANSACTIONS_BY_PORTFOLIO = "SELECT st.* FROM StockTransaction st " +
                "JOIN PortfolioBridge pb ON pb.stockTransactionID = st.stockTransactionID " +
                "JOIN Portfolio p ON p.portfolioID = pb.portfolioID " +
                "WHERE p.portfolioID = ?";

        List<StockTransaction> transactions = jdbcTemplate.query(GET_TRANSACTIONS_BY_PORTFOLIO, new StockTransactionMapper(), portfolioID);

        return transactions;
    }

    private List<PortfolioStock> getPortfolioStocksByPortfolio(int portfolioID) {
        final String GET_PORTFOLIO_STOCKS_BY_PORTFOLIO = "SELECT * FROM PortfolioStock WHERE portfolioID = ?";

        List<PortfolioStock> portfolioStocks = jdbcTemplate.query(GET_PORTFOLIO_STOCKS_BY_PORTFOLIO, new PortfolioStockMapper(), portfolioID);

        return portfolioStocks;
    }

    private Customer getCustomerByPortfolio(int portfolioID) {
        try{
            final String GET_CUSTOMER_BY_PORTFOLIO = "SELECT c.* FROM Customer c " +
                    "JOIN Portfolio p ON p.customerID = c.customerID " +
                    "WHERE p.portfolioID = ?";

            return jdbcTemplate.queryForObject(GET_CUSTOMER_BY_PORTFOLIO, new CustomerMapper(), portfolioID);
        } catch (DataAccessException e) {
            return null;
        }
    }

}
