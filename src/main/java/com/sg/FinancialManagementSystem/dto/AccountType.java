package com.sg.FinancialManagementSystem.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
public class AccountType {
    private int accountTypeID;
    private BankAccountType type;
    private BigDecimal minimumStartDeposit;
    private BigDecimal interestRate;
    private CompoundRate compoundRate;
    private List<Account> accounts;

    public int getAccountTypeID() {
        return accountTypeID;
    }

    public void setAccountTypeID(int accountTypeID) {
        this.accountTypeID = accountTypeID;
    }

    public BankAccountType getType() {
        return type;
    }

    public void setType(BankAccountType type) {
        this.type = type;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public CompoundRate getCompoundRate() {
        return compoundRate;
    }

    public void setCompoundRate(CompoundRate compoundRate) {
        this.compoundRate = compoundRate;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public BigDecimal getMinimumStartDeposit() {
        return minimumStartDeposit;
    }

    public void setMinimumStartDeposit(BigDecimal minimumStartDeposit) {
        this.minimumStartDeposit = minimumStartDeposit;
    }
}
