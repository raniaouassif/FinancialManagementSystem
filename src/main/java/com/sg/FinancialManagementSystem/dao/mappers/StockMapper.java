package com.sg.FinancialManagementSystem.dao.mappers;

import com.sg.FinancialManagementSystem.dto.Company;
import com.sg.FinancialManagementSystem.dto.ExchangeOrganization;
import com.sg.FinancialManagementSystem.dto.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.swing.tree.TreePath;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
@Component
public class StockMapper implements RowMapper<Stock> {
    @Override
    public Stock mapRow(ResultSet rs, int index) throws SQLException {
        Stock stock = new Stock();
        stock.setStockID(rs.getInt("stockID"));
        stock.setTickerCode(rs.getString("tickerCode"));
        stock.setSharePrice(rs.getBigDecimal("sharePrice"));
        stock.setNumberOfOutstandingShares(rs.getLong("numberOfOutstandingShares"));
        stock.setDailyVolume(rs.getLong("dailyVolume"));
//        stock.setCompany(getCompanyByStock(stock));
        return stock;
    }
}
