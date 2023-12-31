package com.sg.FinancialManagementSystem.dao.mappers;

import com.sg.FinancialManagementSystem.dto.Transaction;
import com.sg.FinancialManagementSystem.dto.TransactionType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author raniaouassif on 2023-07-23
 */
public class TransactionMapper implements RowMapper<Transaction> {
    @Override
    public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionID(rs.getInt("transactionID"));
        transaction.setDateTime(rs.getTimestamp("dateTime").toLocalDateTime());
        transaction.setTransactionType(TransactionType.valueOf(rs.getString("transactionType")));
        transaction.setAmount(rs.getBigDecimal("amount"));
        return transaction;
    }
}