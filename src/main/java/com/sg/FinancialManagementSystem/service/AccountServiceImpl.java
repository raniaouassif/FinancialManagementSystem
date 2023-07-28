package com.sg.FinancialManagementSystem.service;

import com.sg.FinancialManagementSystem.dao.AccountDao;
import com.sg.FinancialManagementSystem.dao.BankDao;
import com.sg.FinancialManagementSystem.dao.CustomerDao;
import com.sg.FinancialManagementSystem.dto.Account;
import com.sg.FinancialManagementSystem.dto.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-27
 */
@Service
public class AccountServiceImpl implements AccountService{
    @Autowired
    AccountDao accountDao;

    @Autowired
    CustomerDao customerDao;

    @Autowired
    BankDao bankDao;
    @Override
    public Account getAccountByID(int accountID) {
        return accountDao.getAccountByID(accountID);
    }

    @Override
    public List<Account> getAllAccounts() {
        return null;
    }

    @Override
    public Account addAccount(Account account) {
        return null;
    }

    @Override
    public void updateAccount(Account account) {

    }

    @Override
    public void deleteAccountByID(int accountID) {

    }

    @Override
    public List<Account> getAccountsByCustomer(Customer customer) {
        return null;
    }

    @Override
    public List<Account> getOpenAccountsByCustomer(Customer customer) {
        return null;
    }
}
