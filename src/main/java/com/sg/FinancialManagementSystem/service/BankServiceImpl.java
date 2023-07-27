package com.sg.FinancialManagementSystem.service;

import com.sg.FinancialManagementSystem.dao.BankDao;
import com.sg.FinancialManagementSystem.dto.Account;
import com.sg.FinancialManagementSystem.dto.Bank;
import com.sg.FinancialManagementSystem.dto.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-27
 */

@Service
public class BankServiceImpl implements BankService{
    @Autowired
    BankDao bankDao;

    @Override
    public Bank getBankByID(int bankID) {
        return bankDao.getBankByID(bankID);
    }

    @Override
    public List<Bank> getAllBanks() {
        return bankDao.getAllBanks();
    }

    @Override
    public Bank addBank(Bank bank) {
        return bankDao.addBank(bank);
    }

    @Override
    public void updateBank(Bank bank) {
        bankDao.updateBank(bank);
    }

    @Override
    public void deleteBankByID(int bankID) {
        bankDao.deleteBankByID(bankID);
    }

    @Override
    public List<Bank> getBanksByCustomer(Customer customer) {
        return bankDao.getBanksByCustomer(customer);
    }

    @Override
    public Bank getBankByAccount(Account account) {
        return bankDao.getBankByAccount(account);
    }
}
