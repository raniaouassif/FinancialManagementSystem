package com.sg.FinancialManagementSystem.dao.mappers;

import com.sg.FinancialManagementSystem.dto.Account;
import com.sg.FinancialManagementSystem.dto.AccountTransaction;
import com.sg.FinancialManagementSystem.dto.AccountTransactionType;
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
public class AccountTransactionMapper implements RowMapper<AccountTransaction> {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public AccountTransaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        AccountTransaction transaction = new AccountTransaction();
        transaction.setAccountTransactionID(rs.getInt("accountTransactionID"));
        transaction.setDateTime(rs.getTimestamp("dateTime").toLocalDateTime());
        transaction.setTransactionType(AccountTransactionType.valueOf(rs.getString("transactionType")));
        transaction.setAmount(rs.getBigDecimal("amount"));
        transaction.setAccount(getAccountByAccountTransaction(transaction));
        return null;
    }

    //PRIVATE HELPER FUNCTION
    private Account getAccountByAccountTransaction(AccountTransaction accountTransaction) {

        try {
            final String GET_ACCOUNT_BY_ACCOUNT_TRANSACTION = "SELECT a.* FROM Account a "
                    + "JOIN AccountTransaction at ON at.accountID = a.accountID "
                    + "WHERE at.accountTransactionID = ?";


            Account retrievedAccount = jdbcTemplate.queryForObject(
                    GET_ACCOUNT_BY_ACCOUNT_TRANSACTION,
                    new AccountMapper(),
                    accountTransaction.getAccountTransactionID()
            );
            return retrievedAccount;
        } catch (DataAccessException e ) {
            System.out.println("AccountMapper : No account associated to accountTransaction.");
            return null;
        }
    }
}
