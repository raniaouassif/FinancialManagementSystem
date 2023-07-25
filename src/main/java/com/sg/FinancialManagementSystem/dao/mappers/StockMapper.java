package com.sg.FinancialManagementSystem.dao.mappers;

import com.sg.FinancialManagementSystem.dto.Stock;
import com.sg.FinancialManagementSystem.dto.StockStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author raniaouassif on 2023-07-23
 */
public class StockMapper implements RowMapper<Stock> {
    @Override
    public Stock mapRow(ResultSet rs, int index) throws SQLException {
        Stock stock = new Stock();
        stock.setStockID(rs.getInt("stockID"));
        stock.setTickerCode(rs.getString("tickerCode"));
        stock.setStatus(StockStatus.valueOf(rs.getString("status")));
        stock.setSharePrice(rs.getBigDecimal("sharePrice"));
        stock.setNumberOfOutstandingShares(rs.getLong("numberOfOutstandingShares"));
        stock.setMarketCap(rs.getBigDecimal("marketCap"));
        stock.setDailyVolume(rs.getLong("dailyVolume"));
        return stock;
    }
}
