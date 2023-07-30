package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dao.mappers.AccountMapper;
import com.sg.FinancialManagementSystem.dao.mappers.CustomerMapper;
import com.sg.FinancialManagementSystem.dao.mappers.TransactionMapper;
import com.sg.FinancialManagementSystem.dto.Account;
import com.sg.FinancialManagementSystem.dto.Customer;
import com.sg.FinancialManagementSystem.dto.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-24
 */

@Repository
public class TransactionDaoDB implements TransactionDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public Transaction getTransactionByID(int transactionID) {
        try {
            final String GET_TRANSACTION_BY_ID = "SELECT * FROM Transaction WHERE transactionID = ?";

            Transaction transaction = jdbcTemplate.queryForObject(GET_TRANSACTION_BY_ID, new TransactionMapper(), transactionID);

            // Set the account(s) of the transaction
            setAccountFromAndToForTransaction(transaction);

            return transaction;
        } catch (DataAccessException e) {
            System.out.println("TransactionDaoDB: getTransactionByID() failed. ");
            return null;
        }
    }

    @Override
    public List<Transaction> getAllTransactions() {
        final String GET_ALL_TRANSACTIONS = "SELECT * FROM Transaction";

        List<Transaction> retrievedTransactions = jdbcTemplate.query(GET_ALL_TRANSACTIONS, new TransactionMapper());

        // Set the accounts for each transaction
        setAccountsForTransactionsList(retrievedTransactions);

        return retrievedTransactions;
    }

    @Override
    public Transaction addTransaction(Transaction transaction) {
        final String ADD_TRANSACTION = "INSERT INTO Transaction (dateTime, transactionType, amount) VALUES (?,?,?)";

        jdbcTemplate.update(ADD_TRANSACTION,
                Timestamp.valueOf(transaction.getDateTime()),
                transaction.getTransactionType().toString(),
                transaction.getAmount());

        //Retrieve the generated transactionID and sets it in the transaction object
        int newID = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        transaction.setTransactionID(newID);

        //Insert the account transaction into the AccountTransaction bridge table
        insertAccountTransaction(transaction);

        return transaction;
    }

    @Override
    public void deleteTransactionByID(int transactionID) {
        //First delete from AccountTransaction bridge table
        final String DELETE_ACCOUNT_TRANSACTION_BY_TRANSACTION_ID = "DELETE FROM AccountTransaction WHERE transactionID = ?";
        jdbcTemplate.update(DELETE_ACCOUNT_TRANSACTION_BY_TRANSACTION_ID, transactionID);

        //Then delete the transaction
        final String DELETE_TRANSACTION_BY_ID = "DELETE FROM Transaction WHERE transactionID = ?";
        jdbcTemplate.update(DELETE_TRANSACTION_BY_ID, transactionID);
    }

    @Override
    public List<Transaction> getTransactionsByAccount(int accountID) {
        final String GET_TRANSACTIONS_BY_ACCOUNT = "SELECT t.* FROM Transaction t " +
                "JOIN AccountTransaction at ON at.transactionID = t.transactionID " +
                "JOIN Account a ON a.accountID = at.accountID1 OR a.accountID = at.accountID2 " +
                "WHERE a.accountID = ?";

        List<Transaction> transactions = jdbcTemplate.query(GET_TRANSACTIONS_BY_ACCOUNT, new TransactionMapper(), accountID);

        // Set the accounts for each transaction
        setAccountsForTransactionsList(transactions);

        return transactions.size() == 0 ? new ArrayList<>() : transactions;
    }

    @Override
    public List<Transaction> getASCTransactionsByAccount(int accountID) {
        final String GET_ASC_TRANSACTIONS_BY_ACCOUNT = "SELECT t.* FROM Transaction t " +
                "JOIN AccountTransaction at ON at.transactionID = t.transactionID " +
                "JOIN Account a ON a.accountID = at.accountID1 OR a.accountID = at.accountID2 " +
                "WHERE a.accountID = ? "+
                "ORDER BY t.dateTime ASC";

        List<Transaction> transactions = jdbcTemplate.query(GET_ASC_TRANSACTIONS_BY_ACCOUNT, new TransactionMapper(), accountID);

        // Set the accounts for each transaction
        setAccountsForTransactionsList(transactions);

        return transactions.size() == 0 ? new ArrayList<>() : transactions;
    }

    @Override
    public List<Transaction> getDESCTransactionsByAccount(Integer accountID) {
        final String GET_DESC_TRANSACTIONS_BY_ACCOUNT = "SELECT t.* FROM Transaction t " +
                "JOIN AccountTransaction at ON at.transactionID = t.transactionID " +
                "JOIN Account a ON a.accountID = at.accountID1 OR a.accountID = at.accountID2 " +
                "WHERE a.accountID = ? "+
                "ORDER BY t.dateTime DESC";

        List<Transaction> transactions = jdbcTemplate.query(GET_DESC_TRANSACTIONS_BY_ACCOUNT, new TransactionMapper(), accountID);

        // Set the accounts for each transaction
        setAccountsForTransactionsList(transactions);

        return transactions.size() == 0 ? new ArrayList<>() : transactions;
    }

    // PRIVATE HELPER FUNCTION
    private List<Account> getAccountsByTransaction(Transaction transaction) {
        final String GET_ACCOUNTS_BY_TRANSACTION = "SELECT a.* FROM Account a " +
                "JOIN AccountTransaction at ON at.accountID1 = a.accountID OR at.accountID2 = a.accountID " +
                "JOIN Transaction t ON t.transactionID = at.transactionID " +
                "WHERE t.transactionID = ?";

        List<Account> accounts = jdbcTemplate.query(GET_ACCOUNTS_BY_TRANSACTION, new AccountMapper(), transaction.getTransactionID());

        //TODO set anyhting for accounts ?

        return accounts.size() == 0 ? new ArrayList<>() : accounts;
    }

    //Get the accounts per transaction
    //Returns 1 if the transaction is through the same account
    //Returns 2 if the transaction is from an account to another
    private int accountsPerTransaction(Transaction transaction) {
        return getAccountsByTransaction(transaction).size();
    }

    //Set the accounts From and To for a specific transaction
    private void setAccountFromAndToForTransaction(Transaction transaction) {
        int accountsPerTransaction = accountsPerTransaction(transaction);
        if(accountsPerTransaction == 2 ) {
            Account from = getAccountsByTransaction(transaction).get(1);
            from.setCustomer(getCustomerByAccount(from.getAccountID()));

            Account to = getAccountsByTransaction(transaction).get(0);
            to.setCustomer(getCustomerByAccount(to.getAccountID()));

            transaction.setFrom(from);
            transaction.setTo(to);
        } else if(accountsPerTransaction == 1 ) {
            Account currentAccount = getAccountsByTransaction(transaction).get(0);
            currentAccount.setCustomer(getCustomerByAccount(currentAccount.getAccountID()));
            transaction.setFrom(currentAccount);
            transaction.setTo(currentAccount);
        }
    }

    //Set the accounts From and To for a list of transactions
    private void setAccountsForTransactionsList(List<Transaction> transactionsList) {
        for(Transaction transaction : transactionsList) {
            setAccountFromAndToForTransaction(transaction);
        }
    }

    // Insert a new transaction using From and To
    private void insertAccountTransaction(Transaction transaction) {
        if(transaction.getFrom() != null && transaction.getTo() != null ) {
            final String INSERT_ACCOUNT_TRANSACTION = "INSERT INTO "
                    + "AccountTransaction (transactionID, accountID1, accountID2) "
                    + "VALUES (?,?,?) ";

            jdbcTemplate.update(INSERT_ACCOUNT_TRANSACTION,
                    transaction.getTransactionID(),
                    transaction.getFrom().getAccountID(),
                    transaction.getTo().getAccountID()
            );

            if(transaction.getFrom().getAccountID() != transaction.getTo().getAccountID()) { // TRANSFER FROM AN ACCOUNT TO ANOTHER
                int accountFromID = transaction.getFrom().getAccountID();
                int accountToID = transaction.getTo().getAccountID();



            } else if(transaction.getFrom().getAccountID() == transaction.getTo().getAccountID()) { //DEPOSIT - WITHDRAW FROM SAME ACCOUNT
                int accountID = transaction.getFrom().getAccountID();

                jdbcTemplate.update(INSERT_ACCOUNT_TRANSACTION,
                        transaction.getTransactionID(),
                        accountID,
                        accountID);
            }
        }
    }


    //Get customer by account
    private Customer getCustomerByAccount(int accountID) {
        try {
            final String GET_CUSTOMER_BY_ACCOUNT = "SELECT c.* FROM Customer c "
                    + "JOIN Account a ON a.customerID = c.customerID "
                    + "WHERE a.accountID = ?";

            Customer retrievedCustomer = jdbcTemplate.queryForObject(
                    GET_CUSTOMER_BY_ACCOUNT,
                    new CustomerMapper(),
                    accountID
            );

            //todo set ?

            return retrievedCustomer;
        } catch (DataAccessException e) {
            return null;
        }
    }
}
