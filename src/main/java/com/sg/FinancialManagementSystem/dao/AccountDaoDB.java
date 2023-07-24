package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dao.mappers.AccountMapper;
import com.sg.FinancialManagementSystem.dao.mappers.TransactionMapper;
import com.sg.FinancialManagementSystem.dao.mappers.AccountTypeMapper;
import com.sg.FinancialManagementSystem.dao.mappers.CustomerMapper;
import com.sg.FinancialManagementSystem.dto.Account;
import com.sg.FinancialManagementSystem.dto.Transaction;
import com.sg.FinancialManagementSystem.dto.AccountType;
import com.sg.FinancialManagementSystem.dto.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-24
 */
@Repository
public class AccountDaoDB implements AccountDao{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Account getAccountByID(int accountID) {
        try {
            final String GET_ACCOUNT_BY_ID = "SELECT * FROM Account WHERE accountID = ?";
            Account account = jdbcTemplate.queryForObject(GET_ACCOUNT_BY_ID, new AccountMapper(), accountID);
            // Set the account Type
            // Set the bank
            // Set the list of transactions

        }catch (DataAccessException e){
            System.out.println("AccountDaoDB: getAccountByID() failed.");
            return null;
        }
    }

    @Override
    public List<Account> getAllAccounts() {
        return null;
    }

    @Override
    public Account addAccount(Account account) {
        return null;
    }

    @Override
    public void updateAccount(Account account) {

    }

    @Override
    public void deleteAccountByID(int accountID) {

    }

    @Override
    public List<Account> getAccountsByCustomer(Customer customer) {
        return null;
    }

    @Override
    public List<Account> getAccountsByAccountType(AccountType accountType) {
        return null;
    }

    @Override
    public List<Account> getAccountsByOpeningDate(LocalDate date) {
        return null;
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

    private List<Transaction> getAccountTransactionsByAccount(Account account) {
        final String GET_ACCOUNT_TRANSACTIONS_BY_ACCOUNT = "SELECT * FROM AccountTransaction "
                + "WHERE accountID = ? ";

        List<Transaction> retrievedTransactions = jdbcTemplate.query(
                GET_ACCOUNT_TRANSACTIONS_BY_ACCOUNT,
                new TransactionMapper(),
                account.getAccountID()
        );

        return retrievedTransactions.size() == 0 ? new ArrayList<>() : retrievedTransactions;
    }
}
