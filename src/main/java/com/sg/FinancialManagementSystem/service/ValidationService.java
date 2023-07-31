package com.sg.FinancialManagementSystem.service;

import com.sg.FinancialManagementSystem.dao.AccountDao;
import com.sg.FinancialManagementSystem.dao.AccountTypeDao;
import com.sg.FinancialManagementSystem.dao.BankDao;
import com.sg.FinancialManagementSystem.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-30
 */

@Service
public class ValidationService {

    @Autowired
    AccountDao accountDao;

    @Autowired
    BankDao bankDao;

    @Autowired
    AccountTypeDao accountTypeDao;

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

        if(account.getStatus().equals(AccountStatus.CLOSED)) {//Account has been closed
            message = "Account is closed. No transactions allowed.";
        } else if (transaction.getDateTime().isAfter(LocalDateTime.now())) {
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

        if(accountFrom.getStatus().equals(AccountStatus.CLOSED)) {//Account has been closed
            message = "Account is closed. No transactions allowed.";
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

    public String validateAddCheckingAccountType(AccountType accountType) {
        String message = "";
        List<AccountType> accountTypes = accountTypeDao.getAllCheckingAccountTypes();
        for(AccountType daoAccountType : accountTypes) {
            if(daoAccountType.getMinimumStartDeposit() == accountType.getMinimumStartDeposit()) {
                message = "A checking account with "+ accountType.getMinimumStartDeposit().setScale(2, RoundingMode.HALF_UP)+  " already exists.";
                break;
            }
        }
        return message;
    }

    public String validateAddSavingAccountType(AccountType accountType) {
        String message="";
        if(accountType.getInterestRate().compareTo(BigDecimal.valueOf(100)) > 0 ) {
            message = "Interest rate cannot be greater than 100.";
        }


        List<AccountType> accountTypes = accountTypeDao.getAllSavingsAccountTypes();

        for(AccountType daoAccountType : accountTypes) {
            if(daoAccountType.getType().toString().equals(accountType.getType().toString())
                    && daoAccountType.getCompoundRate().toString().equals(accountType.getCompoundRate().toString())
                    && daoAccountType.getMinimumStartDeposit().equals(accountType.getMinimumStartDeposit())
                    && daoAccountType.getInterestRate().setScale(2, RoundingMode.HALF_UP).equals(accountType.getInterestRate().setScale(2, RoundingMode.HALF_UP))
            ) {
                    message = "A savings account type with the same attributes already exists.";
            }
        }

        return message;
    }

    public String validateAddBank(Bank bank) {
        String message = "";

        List<Bank> banks = bankDao.getAllBanks();

        for(Bank bankDao : banks) {
            if(bankDao.getName().toLowerCase().replaceAll(" ", "")
                    .equals(
                            bank.getName().toLowerCase().replaceAll(" ", "")
                    )) {
                message = "The bank with name " + bank.getName() + " already exists";

            }
        }

        return message;
    }
}
