package com.sg.FinancialManagementSystem.service;

import com.sg.FinancialManagementSystem.dao.AccountDao;
import com.sg.FinancialManagementSystem.dto.Account;
import com.sg.FinancialManagementSystem.dto.Transaction;
import com.sg.FinancialManagementSystem.service.Exceptions.InsufficientFundsException;
import com.sg.FinancialManagementSystem.service.Exceptions.InvalidDateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author raniaouassif on 2023-07-30
 */

@Service
public class TransactionValidationService {

    @Autowired
    AccountDao accountDao;
    public String validateCashTransaction(Transaction transaction) {
        String message = "";

        Account account = accountDao.getAccountByID(transaction.getFrom().getAccountID());
        if(transaction.getDateTime().isAfter(LocalDateTime.now())) {
            message = "Transaction date time can't be in the future";
        }
        else if (transaction.getDateTime().toLocalDate().isBefore(account.getOpeningDate())) {
            message = "Transaction date time can't be before the opening date of the account.";
        } else {
            BigDecimal transactionAmount = transaction.getAmount();
            BigDecimal totalBalance = account.getTotalBalance();
            //If not enough funds
            if(!transaction.getTransactionType().toString().equals("DEPOSIT") && transactionAmount.compareTo(totalBalance) > 0  ) {
                 message = "Not enough funds to " + transaction.getTransactionType().toString().toLowerCase();
            }
        }

        return message;
    }

    public String validateTransferTransaction(Transaction transaction) {
        String message = "";

        Account accountFrom = accountDao.getAccountByID(transaction.getFrom().getAccountID());
        Account accountTo = accountDao.getAccountByID(transaction.getTo().getAccountID());

        //If the transaction date time is in the future
        if(transaction.getDateTime().isAfter(LocalDateTime.now())) {
            message = "Transaction date time can't be in the future";
        }
        //If the transaction date is before any of the accounts
        else if (transaction.getDateTime().toLocalDate().isBefore(accountFrom.getOpeningDate())
        ) {
            message = "Transaction date time can't be before the opening date of the transferring account ["
                + accountFrom.getOpeningDate() + "].";
        } else if (transaction.getDateTime().toLocalDate().isBefore(accountTo.getOpeningDate())
        ) {
            message = "Transaction date time can't be before the opening date of the receiving account ["
                    + accountTo.getOpeningDate() + "].";
        } else if(accountTo.getStatus().toString().equals("CLOSED")) {
            message = "Transaction failed. Receiving account is closed";
        }
        else {
            BigDecimal transactionAmount = transaction.getAmount();
            BigDecimal totalBalance = accountFrom.getTotalBalance();
            //If not enough funds
            if(transactionAmount.compareTo(totalBalance) > 0  ) {
                message = "Not enough funds to make this transfer";
            }
        }
        return message;
    }
}
