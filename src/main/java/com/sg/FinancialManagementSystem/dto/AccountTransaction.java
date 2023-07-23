package com.sg.FinancialManagementSystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author raniaouassif on 2023-07-23
 */
public class AccountTransaction {
    private int accountTransactionID;
    private LocalDateTime dateTime;
    private AccountTransactionType transactionType;
    private BigDecimal amount;

    private Account account;

    public int getAccountTransactionID() {
        return accountTransactionID;
    }

    public void setAccountTransactionID(int accountTransactionID) {
        this.accountTransactionID = accountTransactionID;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public AccountTransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(AccountTransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
