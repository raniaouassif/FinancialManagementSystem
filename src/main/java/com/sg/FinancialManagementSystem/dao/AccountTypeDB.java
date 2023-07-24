package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.Account;
import com.sg.FinancialManagementSystem.dto.AccountType;
import com.sg.FinancialManagementSystem.dto.Bank;
import com.sg.FinancialManagementSystem.dto.Customer;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-24
 */
public class AccountTypeDB implements AccountTypeDao {
    @Override
    public AccountType getAccountTypeByID(int accountTypeID) {
        return null;
    }

    @Override
    public List<AccountType> getAllAccountTypes() {
        return null;
    }

    @Override
    public void updateAccountType(AccountType accountType) {

    }

    @Override
    public void deleteAccountTypeByID(int accountTypeID) {

    }

    @Override
    public List<AccountType> getAccountTypesByBank(Bank bank) {
        return null;
    }

    @Override
    public List<AccountType> getAccountTypesByCustomer(Customer customer) {
        return null;
    }

    @Override
    public AccountType getAccountTypeByAccount(Account account) {
        return null;
    }
}
