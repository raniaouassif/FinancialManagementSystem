package com.sg.FinancialManagementSystem.service;

import com.sg.FinancialManagementSystem.dto.Account;
import com.sg.FinancialManagementSystem.dto.Transaction;
import com.sg.FinancialManagementSystem.service.Exceptions.InsufficientFundsException;
import com.sg.FinancialManagementSystem.service.Exceptions.InvalidDateException;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-29
 */
public interface TransactionService {
    Transaction getTransactionByID(int transactionID);

    List<Transaction> getAllTransactions();

    Transaction addTransaction(Transaction transaction) throws InsufficientFundsException, InvalidDateException;

    void deleteTransactionByID(int transactionID);
    List<Transaction> getTransactionsByAccount(int accountID);

    List<Transaction> getASCTransactionsByAccount(int accountID);

    List<Transaction> getDESCTransactionsByAccounts(Integer accountID);

    List<Transaction> geDESCTransactions();
}
