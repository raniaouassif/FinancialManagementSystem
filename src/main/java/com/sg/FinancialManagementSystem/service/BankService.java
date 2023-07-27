package com.sg.FinancialManagementSystem.service;

import com.sg.FinancialManagementSystem.dto.Account;
import com.sg.FinancialManagementSystem.dto.Bank;
import com.sg.FinancialManagementSystem.dto.Customer;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-27
 */
public interface BankService {
    Bank getBankByID(int bankID);

    List<Bank> getAllBanks();

    Bank addBank(Bank bank);

    void updateBank(Bank bank);

    void deleteBankByID(int bankID);

    List<Bank> getBanksByCustomer(Customer customer);

    Bank getBankByAccount(Account account);
}
