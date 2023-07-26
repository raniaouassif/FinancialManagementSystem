package com.sg.FinancialManagementSystem.dao.mappers;

import com.sg.FinancialManagementSystem.dto.StockTransaction;
import com.sg.FinancialManagementSystem.dto.StockTransactionType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author raniaouassif on 2023-07-25
 */
public class StockTransactionMapper implements RowMapper<StockTransaction> {
    @Override
    public StockTransaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        StockTransaction stockTransaction = new StockTransaction();
        stockTransaction.setStockTransactionID(rs.getInt("stockTransactionID"));
        stockTransaction.setDateTime(rs.getTimestamp("dateTime").toLocalDateTime());
        stockTransaction.setType(StockTransactionType.valueOf(rs.getString("transactionType")));
        stockTransaction.setNumberOfShares(rs.getInt("numberOfShares"));
        stockTransaction.setTransactionCost(rs.getBigDecimal("transactionCost"));
        return stockTransaction;
    }
}
