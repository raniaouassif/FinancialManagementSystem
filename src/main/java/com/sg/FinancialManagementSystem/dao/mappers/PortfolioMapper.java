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
public class PortfolioMapper implements RowMapper<Portfolio> {

    @Override
    public Portfolio mapRow(ResultSet rs, int rowNum) throws SQLException {
        Portfolio portfolio = new Portfolio();
        portfolio.setPortfolioID(rs.getInt("portfolioID"));
        portfolio.setBalance(rs.getBigDecimal("balance"));
        portfolio.setBookValue(rs.getBigDecimal("bookValue"));
        portfolio.setMarketValue(rs.getBigDecimal("marketValue"));
        portfolio.setTotalReturn(rs.getBigDecimal("totalReturn"));
        portfolio.setPercentageReturn(rs.getBigDecimal("percentageReturn"));
        //The Customer, stocks, transactions are set in private methods to get full objects
        return portfolio;
    }
}
