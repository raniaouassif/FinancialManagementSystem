package com.sg.FinancialManagementSystem.service;

import com.sg.FinancialManagementSystem.dao.AccountDao;
import com.sg.FinancialManagementSystem.dao.AccountTypeDao;
import com.sg.FinancialManagementSystem.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-27
 */
@Service
public class AccountTypeServiceImpl implements AccountTypeService{
    @Autowired
    AccountTypeDao accountTypeDao;
    @Autowired
    AccountDao accountDao;

    @Override
    public AccountType getAccountTypeByID(int accountTypeID) {
        return accountTypeDao.getAccountTypeByID(accountTypeID);
    }

    @Override
    public List<AccountType> getAllAccountTypes() {
        return accountTypeDao.getAllAccountTypes();
    }

    @Override
    public AccountType addAccountType(AccountType accountType) {
        return accountTypeDao.addAccountType(accountType);
    }

    @Override
    public void updateAccountType(AccountType accountType) {
        accountTypeDao.updateAccountType(accountType);
    }

    @Override
    public void deleteAccountTypeByID(int accountTypeID) {
        accountTypeDao.deleteAccountTypeByID(accountTypeID);
    }

    @Override
    public List<AccountType> getAccountTypesByBank(Bank bank) {
        return accountTypeDao.getAccountTypesByBank(bank);
    }

    @Override
    public List<AccountType> getAccountTypesByCustomer(Customer customer) {
        return accountTypeDao.getAccountTypesByCustomer(customer);
    }

    @Override
    public AccountType getAccountTypeByAccount(Account account) {
        return accountTypeDao.getAccountTypeByAccount(account);
    }

    @Override
    public List<AccountType> getAccountTypeByType(BankAccountType type) {
        return accountTypeDao.getAccountTypeByType(type);
    }

    @Override
    public List<AccountType> getAllSavingsAccountTypes() {
        return accountTypeDao.getAllSavingsAccountTypes();
    }

    @Override
    public List<AccountType> getAllCheckingAccountTypes() {
        return accountTypeDao.getAllCheckingAccountTypes();
    }
}
