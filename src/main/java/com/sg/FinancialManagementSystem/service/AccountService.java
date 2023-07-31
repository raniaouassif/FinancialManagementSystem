package com.sg.FinancialManagementSystem.service;

import com.sg.FinancialManagementSystem.dto.Account;
import com.sg.FinancialManagementSystem.dto.Customer;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-27
 */
public interface AccountService {
    Account getAccountByID(int accountID);

    List<Account> getAllAccounts();

    Account addAccount(Account account) ;

    void updateAccount(Account account);

    void deleteAccountByID(int accountID);

    List<Account> getAccountsByCustomer(Customer customer);

    List<Account> getOpenAccountsByCustomer(Customer customer);

    public List<Account> getClosedAccountsByCustomer(Customer customer);
}
