package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dao.TransactionDao;
import com.sg.FinancialManagementSystem.dao.mappers.TransactionMapper;
import com.sg.FinancialManagementSystem.dto.Account;
import com.sg.FinancialManagementSystem.dto.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
            return jdbcTemplate.queryForObject(GET_TRANSACTION_BY_ID, new TransactionMapper(), transactionID);
        } catch (DataAccessException e) {
            System.out.println("TransactionDaoDB: getTransactionByID() failed. ");
            return null;
        }
    }

    @Override
    public List<Transaction> getAllTransactions() {
        final String GET_ALL_TRANSACTIONS = "SELECT * FROM Transaction";
        return jdbcTemplate.query(GET_ALL_TRANSACTIONS, new TransactionMapper());
    }

    @Override
    public Transaction addTransaction(Transaction transaction) {
        final String ADD_TRANSACTION = "INSERT INTO Transaction (dateTime, transactionType, amount) VALUES (?,?,?)";
        return null;
    }

    @Override
    public void updateTransaction(Transaction transaction) {

    }

    @Override
    public void deleteTransactionByID(int transactionID) {

    }

    @Override
    public List<Transaction> getTransactionsByAccount(Account account) {
        return null;
    }

    // PRIVATE HELPER FUNCTION
    private List<Account> getAccountsByTransaction(Transaction transaction) {
        final String GET_ACCOUNTS_BY_TRANSACTION = "SELECT a.* FROM "
    }
}
