package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dao.mappers.AccountMapper;
import com.sg.FinancialManagementSystem.dao.mappers.TransactionMapper;
import com.sg.FinancialManagementSystem.dto.Account;
import com.sg.FinancialManagementSystem.dto.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
                LocalDateTime.now().withNano(0),
                transaction.getTransactionType().toString(),
                transaction.getAmount());

        //Retrieve the generated transactionID and sets it in the transaction object
        int newID = jdbcTemplate.update("SELECT LAST_INSERT_ID()", Integer.class);
        transaction.setTransactionID(newID);

        //Insert the account transaction into the AccountTransaction bridge table
        insertAccountTransaction(transaction);

        return transaction;
    }

    @Override
    public List<Transaction> getTransactionsByAccount(Account account) {
        final String GET_TRANSACTIONS_BY_ACCOUNT = "SELECT t.* FROM Transaction t " +
                "JOIN AccountTransaction at ON at.transactionID = t.transactionID " +
                "JOIN Account a ON a.accountID = at.accountID1 OR a.accountID = at.accountID2 " +
                "WHERE a.accountID = ?";

        List<Transaction> transactions = jdbcTemplate.query(GET_TRANSACTIONS_BY_ACCOUNT, new TransactionMapper(), account.getAccountID());

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
            transaction.setFrom(getAccountsByTransaction(transaction).get(0));
            transaction.setTo(getAccountsByTransaction(transaction).get(1));
        } else if(accountsPerTransaction == 1 ) {
            transaction.setFrom(getAccountsByTransaction(transaction).get(0));
            transaction.setTo(getAccountsByTransaction(transaction).get(0));
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
                    + "AccountTransaction (transactionID, accoundID1, accoundID2) "
                    + "VALUES (?,?,?) ";

            if(transaction.getFrom().getAccountID() != transaction.getTo().getAccountID()) { // TRANSFER FROM AN ACCOUNT TO ANOTHER
                int accountFromID = transaction.getFrom().getAccountID();
                int accountToID = transaction.getTo().getAccountID();

                jdbcTemplate.update(INSERT_ACCOUNT_TRANSACTION,
                        transaction.getTransactionID(),
                        accountFromID,
                        accountToID);

            } else if(transaction.getFrom().getAccountID() == transaction.getTo().getAccountID()) { //DEPOSIT - WITHDRAW FROM SAME ACCOUNT
                int accountID = transaction.getFrom().getAccountID();

                jdbcTemplate.update(INSERT_ACCOUNT_TRANSACTION,
                        transaction.getTransactionID(),
                        accountID,
                        accountID);
            }
        }
    }
}
