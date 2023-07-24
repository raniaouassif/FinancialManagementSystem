package com.sg.FinancialManagementSystem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
public class Account {
    private int accountID;
    private LocalDate openingDate;
    private BigDecimal depositBalance;
    private BigDecimal interestBalance;
    private BigDecimal totalBalance;
    private AccountStatus status;
    private LocalDate closingDate;
    private String closingReason;
    private Bank bank;
    private AccountType accountType;
    private Customer customer;
    private List<Transaction> transactions;

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    public BigDecimal getDepositBalance() {
        return depositBalance;
    }

    public void setDepositBalance(BigDecimal depositBalance) {
        this.depositBalance = depositBalance;
    }

    public BigDecimal getInterestBalance() {
        return interestBalance;
    }

    public void setInterestBalance(BigDecimal interestBalance) {
        this.interestBalance = interestBalance;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public List<Transaction> getAccountTransactions() {
        return transactions;
    }

    public void setAccountTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }


    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public LocalDate getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
    }

    public String getClosingReason() {
        return closingReason;
    }

    public void setClosingReason(String closingReason) {
        this.closingReason = closingReason;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }
}
