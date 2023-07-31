package com.sg.FinancialManagementSystem.service;

import com.sg.FinancialManagementSystem.dto.*;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-27
 */
public interface AccountTypeService {
    AccountType getAccountTypeByID(int accountTypeID);

    List<AccountType> getAllAccountTypes();

    AccountType addAccountType(AccountType accountType);

    void updateAccountType(AccountType accountType);

    void deleteAccountTypeByID(int accountTypeID);

    List<AccountType> getAccountTypesByBank(Bank bank);

    List<AccountType> getAccountTypesByCustomer(Customer customer);

    AccountType getAccountTypeByAccount(Account account);
    List<AccountType> getAccountTypeByType(BankAccountType type);


    public List<AccountType> getAllSavingsAccountTypes();

    public List<AccountType> getAllCheckingAccountTypes();
}
