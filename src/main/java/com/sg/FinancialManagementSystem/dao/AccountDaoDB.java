package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dao.mappers.*;
import com.sg.FinancialManagementSystem.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

            //Set customer, accountType, bank and list of transactions for account.
            setCustomerTypeAndBankAndTransactionsForAccount(account);

            // TODO SET THE BALANCES ?

            return account;
        }catch (DataAccessException e){
            return null;
        }
    }

    @Override
    public List<Account> getAllAccounts() {
        final String GET_ALL_ACCOUNTS = "SELECT * FROM Account";
        List<Account> accounts = jdbcTemplate.query(GET_ALL_ACCOUNTS, new AccountMapper());

        //Set accountType, bank and list of transactions for the accounts
        setCustomerTypeAndBankAndTransactionsForAccountList(accounts);
        return accounts;
    }

    @Override
    public Account addAccount(Account account) {
        final String ADD_ACCOUNT = "INSERT INTO Account "
                +   "(openingDate, depositBalance, status, customerID) "
                +   " VALUES (?,?,?,?)";

        jdbcTemplate.update(ADD_ACCOUNT,
                account.getOpeningDate(),
                new BigDecimal(0).setScale(2, RoundingMode.HALF_UP), // the starting deposit balance is $0.00
                AccountStatus.OPEN.toString(), // SET THE STATUS TO OPEN
                account.getCustomer().getCustomerID());

        //Set the account ID
        int newID = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        account.setAccountID(newID);
        //Some fields are auto-generated
        account.setStatus(AccountStatus.OPEN);
        account.setDepositBalance(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP));

        //Insert into the Account bridge table
        insertAccountBridge(account);

        return account;
    }

    @Override
    public void updateAccount(Account account) {
        final String UPDATE_ACCOUNT = "UPDATE Account SET " +
                "depositBalance = ?, interestBalance = ?, totalBalance = ?, status = ?, closingDate = ?, closingReason = ? " +
                "WHERE accountID = ?";

        jdbcTemplate.update(UPDATE_ACCOUNT,
                account.getDepositBalance(),
                account.getInterestBalance(),
                account.getTotalBalance(),
                account.getStatus().toString(),
                LocalDate.now(),
                account.getClosingReason(),
                account.getAccountID()
                );

    }

    @Override
    public void deleteAccountByID(int accountID) {
//        //Can't delete an account -> set to closed
//        final String CLOSE_ACCOUNT = "UPDATE Account SET " +
//                "depositBalance = 0.00, interestBalance = 0.00, totalBalance = 0.00, status = ?, closingDate = ?, closingReason = ? " +
//                "WHERE accountID = ?";
//
//        jdbcTemplate.update(CLOSE_ACCOUNT,
//                AccountStatus.CLOSED.toString(),
//                LocalDate.now(),
//                "Account deleted.",
//                accountID);
//
        // First delete from the AccountBridge table
        final String DELETE_ACCOUNT_BRIDGE_BY_ACCOUNT = "DELETE FROM AccountBridge WHERE accountID = ?";
        jdbcTemplate.update(DELETE_ACCOUNT_BRIDGE_BY_ACCOUNT, accountID);

        //Then delete all the related transactions
        final String DELETE_ACCOUNT_TRANSACTIONS_BY_ACCOUNT = "DELETE FROM AccountTransaction WHERE accountID1 = ? OR accountID2 = ?";
        jdbcTemplate.update(DELETE_ACCOUNT_TRANSACTIONS_BY_ACCOUNT, accountID, accountID);

        //Finally delete the account
        final String DELETE_ACCOUNT = "DELETE FROM Account where AccountID = ?";
        jdbcTemplate.update(DELETE_ACCOUNT, accountID);

    }

    @Override
    public List<Account> getAccountsByCustomer(Customer customer) {
        final String GET_ACCOUNTS_BY_CUSTOMER = "SELECT a.* FROM Account a " +
                "JOIN Customer c ON c.customerID = a.customerID " +
                "WHERE c.customerID = ? ";

        List<Account> accounts = jdbcTemplate.query(GET_ACCOUNTS_BY_CUSTOMER, new AccountMapper(), customer.getCustomerID());

        setCustomerTypeAndBankAndTransactionsForAccountList(accounts);

        return accounts.size() == 0 ? new ArrayList<>() : accounts;
    }

    @Override
    public List<Account> getOpenAccountsByCustomer(Customer customer) {
        final String GET_OPEN_ACCOUNTS_BY_CUSTOMER = "SELECT a.* FROM Account a " +
                "JOIN Customer c ON c.customerID = a.customerID " +
                "WHERE a.status = 'OPEN' AND c.customerID = ?";

        List<Account> accounts = jdbcTemplate.query(GET_OPEN_ACCOUNTS_BY_CUSTOMER, new AccountMapper(), customer.getCustomerID());

        setCustomerTypeAndBankAndTransactionsForAccountList(accounts);
        return accounts.size() == 0 ? new ArrayList<>() : accounts;
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

            //todo set ?

            return retrievedCustomer;
        } catch (DataAccessException e) {
            return null;
        }
    }

    private Bank getBankByAccount(Account account) {
        try {
            final String GET_BANK_BY_ACCOUNT = "SELECT b.* FROM Bank b " +
                    "JOIN BankAccountType bat ON bat.bankID = b.bankID " +
                    "JOIN AccountBridge ba ON ba.bankID = bat.bankID AND ba.accountTypeID = bat.accountTypeID " +
                    "JOIN Account a ON a.accountID = ba.accountID " +
                    "WHERE a.accountID = ?";

            // TODO : ANYTHING TO SET ?

            return jdbcTemplate.queryForObject(GET_BANK_BY_ACCOUNT, new BankMapper(), account.getAccountID());
        } catch (DataAccessException e) {
            return null;
        }
    }

    private AccountType getAccountTypeByAccount(Account account) {
        try {
            final String GET_ACCOUNT_TYPE_BY_ACCOUNT = "SELECT at.* FROM AccountType at " +
                    "JOIN BankAccountType bat ON bat.accountTypeID = at.accountTypeID " +
                    "JOIN AccountBridge ba ON ba.accountTypeID = bat.accountTypeID AND ba.bankID = bat.bankID " +
                    "JOIN Account a ON a.accountID = ba.accountID " +
                    "WHERE a.accountID = ?";

            AccountType retrievedAccountType = jdbcTemplate.queryForObject(
                    GET_ACCOUNT_TYPE_BY_ACCOUNT,
                    new AccountTypeMapper(),
                    account.getAccountID()
            );

            // TODO : ANYTHING TO SET ?
            return retrievedAccountType;
        } catch (DataAccessException e) {
            return null;
        }
    }

    private List<Transaction> getAccountTransactionsByAccount(Account account) {
        //todo
        final String GET_ACCOUNT_TRANSACTIONS_BY_ACCOUNT = "SELECT t.* FROM Transaction t " +
                "JOIN AccountTransaction at ON at.transactionID = t.transactionID " +
                "JOIN Account a ON a.accountID = at.accountID1 OR a.accountID = at.accountID2 " +
                "WHERE a.accountID = ?";

        List<Transaction> retrievedTransactions = jdbcTemplate.query(
                GET_ACCOUNT_TRANSACTIONS_BY_ACCOUNT,
                new TransactionMapper(),
                account.getAccountID()
        );


        //TODO SET THE THINGS FOR TRANSACTION
        return retrievedTransactions.size() == 0 ? new ArrayList<>() : retrievedTransactions;
    }

    //Set customer, account type, bank and list of transactions for a given account
    private void setCustomerTypeAndBankAndTransactionsForAccount(Account account) {
        account.setCustomer(getCustomerByAccount(account)); // Set the customer
        account.setAccountType(getAccountTypeByAccount(account)); // Set the account Type
        account.setBank(getBankByAccount(account));  // Set the bank
        account.setAccountTransactions(getAccountTransactionsByAccount(account)); // Set the list of transactions
    }

    //Set  customer, account type, bank and list of transactions for a list of accounts
    private void setCustomerTypeAndBankAndTransactionsForAccountList(List<Account> accountList ){
        for(Account account : accountList) {
            setCustomerTypeAndBankAndTransactionsForAccount(account);
        }
    }

    //Insert into the Account bridge
    private void insertAccountBridge(Account account) {
        final String INSERT_ACCOUNT_BRIDGE = "INSERT INTO AccountBridge (accountID, bankID, accountTypeID) VALUES (?,?,?)";
        jdbcTemplate.update(INSERT_ACCOUNT_BRIDGE, account.getAccountID(), account.getBank().getBankID(), account.getAccountType().getAccountTypeID());
    }
    
    
    



}
