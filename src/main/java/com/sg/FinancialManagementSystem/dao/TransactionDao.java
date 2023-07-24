package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.Account;
import com.sg.FinancialManagementSystem.dto.Transaction;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
public interface TransactionDao {
    Transaction getTransactionByID(int transactionID);

    List<Transaction> getAllTransactions();

    Transaction addTransaction(Transaction transaction);

    void updateTransaction(Transaction transaction);

    void deleteTransactionByID(int transactionID);

    List<Transaction> getTransactionsByAccount(Account account);

}
