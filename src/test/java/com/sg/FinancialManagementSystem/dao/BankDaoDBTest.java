package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.AccountType;
import com.sg.FinancialManagementSystem.dto.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author raniaouassif on 2023-07-24
 */
@SpringBootTest
class BankDaoDBTest {

    @Autowired
    BankDao bankDao;
    @BeforeEach
    void setUp() {
//        List<Bank> banks = bankDao.getAllBanks();

    }


    @Test
    @DisplayName("Get and Add Bank By ID")
    void testGetAndAddBankByID() {
        AccountType accountType = new AccountType();
//        accountType.set

        Bank bank = new Bank();
        bank.setName("RBC Royal Bank");
        bank.setLocation("Canada");
        bank.setAccountTypes(new ArrayList<>());
        bank = bankDao.addBank(bank);

        Bank bankFromDao = bankDao.getBankByID(bank.getBankID());
        assertEquals(bank, bankFromDao);
    }

    @Test
    void getAllBanks() {
    }

    @Test
    void addBank() {
    }

    @Test
    void updateBank() {
    }

    @Test
    void deleteBankByID() {
    }

    @Test
    void getBanksByCustomer() {
    }

    @Test
    void getBankByAccount() {
    }
}