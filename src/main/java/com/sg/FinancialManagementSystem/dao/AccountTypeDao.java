package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.*;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-24
 */
public interface AccountTypeDao {
    AccountType getAccountTypeByID(int accountTypeID);

    List<AccountType> getAllAccountTypes();

    AccountType addAccountType(AccountType accountType);

    void updateAccountType(AccountType accountType);

    void deleteAccountTypeByID(int accountTypeID);

    List<AccountType> getAccountTypesByBank(Bank bank);

    List<AccountType> getAccountTypesByCustomer(Customer customer);

    AccountType getAccountTypeByAccount(Account account);

    public List<AccountType> getAllSavingsAccountTypes();

    public List<AccountType> getAllCheckingAccountTypes();
    List<AccountType> getAccountTypeByType(BankAccountType type);
}
