package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.Account;
import com.sg.FinancialManagementSystem.dto.AccountType;
import com.sg.FinancialManagementSystem.dto.Customer;

import java.time.LocalDate;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
public interface AccountDao {
    Account getAccountByID(int accountID);
    List<Account> getAllAccounts();

    Account addAccount(Account account);

    void updateAccount(Account account);

    void deleteAccountByID(int accountID);

    List<Account> getAccountsByCustomer(Customer customer);

    List<Account> getOpenAccountsByCustomer(Customer customer);


}