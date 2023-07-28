package com.sg.FinancialManagementSystem.service;

import com.sg.FinancialManagementSystem.dao.AccountDao;
import com.sg.FinancialManagementSystem.dao.AccountTypeDao;
import com.sg.FinancialManagementSystem.dto.Account;
import com.sg.FinancialManagementSystem.dto.AccountType;
import com.sg.FinancialManagementSystem.dto.Bank;
import com.sg.FinancialManagementSystem.dto.Customer;
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
        return accountTypeDao.getAccountTypesByBank(bank);
    }

    @Override
    public List<AccountType> getAccountTypesByCustomer(Customer customer) {
        return null;
    }

    @Override
    public AccountType getAccountTypeByAccount(Account account) {
        return accountTypeDao.getAccountTypeByAccount(account);
    }
}
