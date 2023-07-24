package com.sg.FinancialManagementSystem.dao.mappers;

import com.sg.FinancialManagementSystem.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
@Component
public class AccountMapper implements RowMapper<Account> {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        Account account = new Account();
        account.setAccountID(rs.getInt("accountID"));
        account.setOpeningDate(rs.getDate("openingDate").toLocalDate());
        account.setDepositBalance(rs.getBigDecimal("depositBalance"));
        account.setInterestBalance(rs.getBigDecimal("interestBalance"));
        account.setTotalBalance(rs.getBigDecimal("totalBalance"));
        account.setStatus(AccountStatus.valueOf(rs.getString("status")));
        account.setClosingDate(rs.getDate("closingDate").toLocalDate());
        account.setClosingReason(rs.getString("closingReason"));
        account.setCustomer(getCustomerByAccount(account));
        account.setAccountType(getAccountTypeByAccount(account));
        account.setAccountTransactions(getAccountTransactionsByAccount(account));
        return account;
    }

    //Private helper functions
    private Customer getCustomerByAccount(Account account) {
        try {
            final String GET_CUSTOMER_BY_ACCOUNT = "SELECT c.* FROM Customer c "
                    + "JOIN Account a ON a.customerID = c.customerID "
                    + "WHERE a.accountID = ?";

            Customer retrievedCustomer = jdbcTemplate.queryForObject(
                    GET_CUSTOMER_BY_ACCOUNT,
                    new CustomerMapper(),
                    account.getAccountID()
            );

            return retrievedCustomer;
        } catch (DataAccessException e) {
            System.out.println("AccountMapper : No customer for given account.");
            return null;
        }
    }

    private AccountType getAccountTypeByAccount(Account account) {
        try {
            final String GET_ACCOUNT_TYPE_BY_ACCOUNT = "SELECT at.* FROM AccountType at "
                    + "JOIN Account a ON a.accountTypeID = at.accountTypeID "
                    + "WHERE a.accountID = ?";

            AccountType retrievedAccountType = jdbcTemplate.queryForObject(
                    GET_ACCOUNT_TYPE_BY_ACCOUNT,
                    new AccountTypeMapper(),
                    account.getAccountID()
            );
            return retrievedAccountType;
        } catch (DataAccessException e) {
            System.out.println("AccountMapper : No customer for given account.");
            return null;
        }
    }

    private List<AccountTransaction> getAccountTransactionsByAccount(Account account) {
        final String GET_ACCOUNT_TRANSACTIONS_BY_ACCOUNT = "SELECT * FROM AccountTransaction "
                + "WHERE accountID = ? ";

        List<AccountTransaction> retrievedTransactions = jdbcTemplate.query(
                GET_ACCOUNT_TRANSACTIONS_BY_ACCOUNT,
                new AccountTransactionMapper(),
                account.getAccountID()
        );

        return retrievedTransactions.size() == 0 ? new ArrayList<>() : retrievedTransactions;
    }
}
