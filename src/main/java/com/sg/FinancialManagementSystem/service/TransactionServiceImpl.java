package com.sg.FinancialManagementSystem.service;

import com.sg.FinancialManagementSystem.dao.AccountDao;
import com.sg.FinancialManagementSystem.dao.TransactionDao;
import com.sg.FinancialManagementSystem.dto.Account;
import com.sg.FinancialManagementSystem.dto.Transaction;
import com.sg.FinancialManagementSystem.service.Exceptions.InsufficientFundsException;
import com.sg.FinancialManagementSystem.service.Exceptions.InvalidDateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    public Transaction addTransaction(Transaction transaction) throws InsufficientFundsException, InvalidDateException {
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
}
