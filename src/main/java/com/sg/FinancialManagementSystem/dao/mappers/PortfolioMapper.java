package com.sg.FinancialManagementSystem.dao.mappers;

import com.sg.FinancialManagementSystem.dto.Customer;
import com.sg.FinancialManagementSystem.dto.Portfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author raniaouassif on 2023-07-23
 */
@Component
public class PortfolioMapper implements RowMapper<Portfolio> {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public Portfolio mapRow(ResultSet rs, int rowNum) throws SQLException {
        Portfolio portfolio = new Portfolio();
        portfolio.setPortfolioID(rs.getInt("portfolioID"));
        portfolio.setBalance(rs.getBigDecimal("balance"));
        portfolio.setCustomer(getCustomerByPortfolio(portfolio));
//        portfolio.setStocks();
        //TODO
        return null;
    }

    //PRIVATE HELPER FUNCITONS

    private Customer getCustomerByPortfolio(Portfolio portfolio) {
        try {
            final String GET_CUSTOMER_BY_PORTFOLIO = "SELECT c.* FROM Customer c " +
                    "JOIN Portfolio p ON p.customerID = c.customerID " +
                    "WHERE p.portfolioID = ?";

            Customer retrievedCustomer = jdbcTemplate.queryForObject(
                    GET_CUSTOMER_BY_PORTFOLIO,
                    new CustomerMapper(),
                    portfolio.getPortfolioID()
            );

            return retrievedCustomer;
        } catch (DataAccessException e) {
            System.out.println("PortfolioMapper: No Customer associated to the portfolio.");
            return null;
        }

    }
}
