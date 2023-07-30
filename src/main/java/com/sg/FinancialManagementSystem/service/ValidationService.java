package com.sg.FinancialManagementSystem.service;

import com.sg.FinancialManagementSystem.dao.AccountDao;
import com.sg.FinancialManagementSystem.dao.BankDao;
import com.sg.FinancialManagementSystem.dto.Account;
import com.sg.FinancialManagementSystem.dto.Transaction;
import com.sg.FinancialManagementSystem.service.Exceptions.InsufficientMinDepositException;
import com.sg.FinancialManagementSystem.service.Exceptions.InvalidBankAccountTypeException;
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
public class ValidationService {

    @Autowired
    AccountDao accountDao;

    @Autowired
    BankDao bankDao;

    public String validateAddAccount(Account account) {
        String message = "";

        BigDecimal minDeposit = account.getAccountType().getMinimumStartDeposit();

        boolean validBankAccountType = bankDao.getBankByID(account.getBank().getBankID()).getAccountTypes().contains(account.getAccountType());

        BigDecimal initialDeposit = account.getDepositBalance();
        //Check if insufficient initial deposit for account type chosen
        if(initialDeposit.compareTo(minDeposit) < 0) {
            message = "Error: The minimum deposit for this account is "+ account.getAccountType().getMinimumStartDeposit();
        } else if(!validBankAccountType) {
            message ="Error:"+ account.getBank().getName() + " does not propose the selected acocunt.";
        } else if(account.getOpeningDate().isAfter(LocalDate.now())) {
            message ="Opening date can't be in the future. ";
        }
        return message;
    }
    public String validateCashTransaction(Transaction transaction) {
        String message = "";

        Account account = accountDao.getAccountByID(transaction.getFrom().getAccountID());
        if(account.getOpeningDate() == null || account.getDepositBalance() == null) {
            message = "Please fill all the fields";
        }else if(transaction.getDateTime().isAfter(LocalDateTime.now())) {
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

        //If any field is not inputed
        if(transaction.getDateTime() == null || transaction.getAmount() == null) {
            message = "Please fill all the fields before submitting.";
        }
        //If the transaction date time is in the future
         else if(transaction.getDateTime().isAfter(LocalDateTime.now())) {
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
