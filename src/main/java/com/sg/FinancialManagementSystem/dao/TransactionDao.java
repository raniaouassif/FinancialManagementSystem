package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.Account;
import com.sg.FinancialManagementSystem.dto.Customer;
import com.sg.FinancialManagementSystem.dto.Transaction;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
public interface TransactionDao {
    Transaction getTransactionByID(int transactionID);

    List<Transaction> getAllTransactions();

    Transaction addTransaction(Transaction transaction);

    // NO UPDATE OR DELETE METHODS

    List<Transaction> getTransactionsByAccount(Account account);


}
