package com.sg.FinancialManagementSystem.service;

import com.sg.FinancialManagementSystem.dao.AccountDao;
import com.sg.FinancialManagementSystem.dao.TransactionDao;
import com.sg.FinancialManagementSystem.dto.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-29
 */
@Service
public class TransactionServiceImpl implements  TransactionService{
    @Autowired
    TransactionDao transactionDao;

    @Autowired
    AccountDao accountDao;
    @Override
    public Transaction getTransactionByID(int transactionID) {
        return transactionDao.getTransactionByID(transactionID);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionDao.getAllTransactions();
    }

    @Override
    public Transaction addTransaction(Transaction transaction)  {
        return transactionDao.addTransaction(transaction);
    }

    @Override
    public void deleteTransactionByID(int transactionID) {
        transactionDao.deleteTransactionByID(transactionID);
    }

    @Override
    public List<Transaction> getTransactionsByAccount(int accountID) {
        return transactionDao.getTransactionsByAccount(accountID);
    }

    @Override
    public List<Transaction> getASCTransactionsByAccount(int accountID) {
        return transactionDao.getASCTransactionsByAccount(accountID);
    }

    @Override
    public List<Transaction> getDESCTransactionsByAccounts(Integer accountID) {
        return transactionDao.getDESCTransactionsByAccount(accountID);
    }

    @Override
    public List<Transaction> geDESCTransactions() {
        return transactionDao.getDESCTransactions();
    }
}
