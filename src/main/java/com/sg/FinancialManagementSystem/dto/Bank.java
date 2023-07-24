package com.sg.FinancialManagementSystem.dto;

import java.util.List;
import java.util.Objects;

/**
 * @author raniaouassif on 2023-07-23
 */
public class Bank {
    private int bankID;
    private String name;
    private String location;
    private List<AccountType> accountTypes;

    public int getBankID() {
        return bankID;
    }

    public void setBankID(int bankID) {
        this.bankID = bankID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AccountType> getAccountTypes() {
        return accountTypes;
    }

    public void setAccountTypes(List<AccountType> accountTypes) {
        this.accountTypes = accountTypes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bank)) return false;
        Bank bank = (Bank) o;
        return getBankID() == bank.getBankID() && Objects.equals(getName(), bank.getName()) && Objects.equals(getLocation(), bank.getLocation()) && Objects.equals(getAccountTypes(), bank.getAccountTypes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBankID(), getName(), getLocation(), getAccountTypes());
    }
}
