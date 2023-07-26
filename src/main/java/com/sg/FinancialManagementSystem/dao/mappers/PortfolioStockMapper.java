package com.sg.FinancialManagementSystem.dao.mappers;

import com.sg.FinancialManagementSystem.dto.PortfolioStock;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author raniaouassif on 2023-07-25
 */
public class PortfolioStockMapper implements RowMapper<PortfolioStock> {
    @Override
    public PortfolioStock mapRow(ResultSet rs, int rowNum) throws SQLException {
        PortfolioStock portfolioStock = new PortfolioStock();
        portfolioStock.setPortfolioStockID(rs.getInt("portfolioStockID"));
        portfolioStock.setNumberOfShares(rs.getInt("numberOfShares"));
        portfolioStock.setMarketValue(rs.getBigDecimal("marketValue"));
        portfolioStock.setBookValue(rs.getBigDecimal("bookValue"));
        portfolioStock.setAveragePrice(rs.getBigDecimal("averagePrice"));
        portfolioStock.setTotalReturn(rs.getBigDecimal("totalReturn"));
        portfolioStock.setPercentageReturn(rs.getBigDecimal("percentageReturn"));

        return portfolioStock;
    }
}
