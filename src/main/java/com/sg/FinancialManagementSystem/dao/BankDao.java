package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.Account;
import com.sg.FinancialManagementSystem.dto.Bank;
import com.sg.FinancialManagementSystem.dto.Customer;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
public interface BankDao {

    Bank getBankByID(int bankID);

    List<Bank> getAllBanks();

    Bank addBank(Bank bank);

    void updateBank(Bank bank);

    void deleteBankByID(int bankID);

    List<Bank> getBanksByCustomer(Customer customer);

    Bank getBankByAccount(Account account);

}
