package com.sg.FinancialManagementSystem.dto;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
public class Bank {
    private int bankID;
    private String name;
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
}
