package com.sg.FinancialManagementSystem.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @author raniaouassif on 2023-07-23
 */
public class AccountType {
    private int accountTypeID;
    private BankAccountType type;
    private BigDecimal minimumStartDeposit;
    private BigDecimal interestRate;
    private CompoundRate compoundRate;
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

    public BigDecimal getMinimumStartDeposit() {
        return minimumStartDeposit;
    }

    public void setMinimumStartDeposit(BigDecimal minimumStartDeposit) {
        this.minimumStartDeposit = minimumStartDeposit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountType)) return false;
        AccountType that = (AccountType) o;
        return getAccountTypeID() == that.getAccountTypeID() && getType() == that.getType() && Objects.equals(getMinimumStartDeposit(), that.getMinimumStartDeposit()) && Objects.equals(getInterestRate(), that.getInterestRate()) && getCompoundRate() == that.getCompoundRate();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccountTypeID(), getType(), getMinimumStartDeposit(), getInterestRate(), getCompoundRate());
    }
}
