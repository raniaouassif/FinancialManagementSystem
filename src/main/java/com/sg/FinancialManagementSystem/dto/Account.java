package com.sg.FinancialManagementSystem.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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
    @NotBlank(message = "Please provide a closing reason.")
    @Size(min = 10, message = "Closing reason should be at least 10 characters.")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return getAccountID() == account.getAccountID() && Objects.equals(getOpeningDate(), account.getOpeningDate()) && Objects.equals(getDepositBalance(), account.getDepositBalance()) && Objects.equals(getInterestBalance(), account.getInterestBalance()) && Objects.equals(getTotalBalance(), account.getTotalBalance()) && getStatus() == account.getStatus() && Objects.equals(getClosingDate(), account.getClosingDate()) && Objects.equals(getClosingReason(), account.getClosingReason()) && Objects.equals(getBank(), account.getBank()) && Objects.equals(getAccountType(), account.getAccountType()) && Objects.equals(getCustomer(), account.getCustomer()) && Objects.equals(transactions, account.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccountID(), getOpeningDate(), getDepositBalance(), getInterestBalance(), getTotalBalance(), getStatus(), getClosingDate(), getClosingReason(), getBank(), getAccountType(), getCustomer(), transactions);
    }
}
