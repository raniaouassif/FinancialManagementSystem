package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.AccountType;
import com.sg.FinancialManagementSystem.dto.Bank;
import com.sg.FinancialManagementSystem.dto.BankAccountType;
import com.sg.FinancialManagementSystem.dto.CompoundRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
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

    @Autowired
    AccountTypeDao accountTypeDao;

    @BeforeEach
    void setUp() {
        // Delete all account types & banks
        List<AccountType> accountTypes = accountTypeDao.getAllAccountTypes();
        for(AccountType accountType : accountTypes) {
            accountTypeDao.deleteAccountTypeByID(accountType.getAccountTypeID());
        }
        List<Bank> banks = bankDao.getAllBanks();
        for(Bank bank : banks) {
            bankDao.deleteBankByID(bank.getBankID());
        }
    }

    @Test
    @DisplayName("Get and Add Bank By ID")
    void testGetAndAddBankByID() {
        //Create new account type : SAVING
        AccountType savingsAccount = new AccountType();
        savingsAccount.setType(BankAccountType.HIGH_INTEREST_SAVINGS);
        savingsAccount.setMinimumStartDeposit(new BigDecimal(100.00).setScale(2));
        savingsAccount.setInterestRate(new BigDecimal(3.5).setScale(2));
        savingsAccount.setCompoundRate(CompoundRate.QUARTERLY);
        savingsAccount = accountTypeDao.addAccountType(savingsAccount);

        //Create new account type : CHECKING
        AccountType checkingAccount = new AccountType();
        checkingAccount.setType(BankAccountType.CHECKING);
        checkingAccount.setMinimumStartDeposit(new BigDecimal(25).setScale(2));
        checkingAccount.setInterestRate(new BigDecimal(0).setScale(2));
        checkingAccount.setCompoundRate(CompoundRate.NA);
        checkingAccount = accountTypeDao.addAccountType(checkingAccount);

        List<AccountType> bankAccountTypes = new ArrayList<>();
        bankAccountTypes.add(savingsAccount);
        bankAccountTypes.add(checkingAccount);

        Bank bank = new Bank();
        bank.setName("RBC Royal Bank");
        bank.setLocation("Canada");
        bank.setAccountTypes(bankAccountTypes);
        bank = bankDao.addBank(bank);

        Bank bankFromDao = bankDao.getBankByID(bank.getBankID());
        assertEquals(bank, bankFromDao);
    }

    @Test
    @DisplayName("Get All Banks")
    void testGetAllBanks() {
        //Create new account type : SAVING
        AccountType savingsAccount = new AccountType();
        savingsAccount.setType(BankAccountType.HIGH_INTEREST_SAVINGS);
        savingsAccount.setMinimumStartDeposit(new BigDecimal(100.00).setScale(2));
        savingsAccount.setInterestRate(new BigDecimal(3.5).setScale(2));
        savingsAccount.setCompoundRate(CompoundRate.QUARTERLY);
        savingsAccount = accountTypeDao.addAccountType(savingsAccount);

        //Create new account type : CHECKING
        AccountType checkingAccount = new AccountType();
        checkingAccount.setType(BankAccountType.CHECKING);
        checkingAccount.setMinimumStartDeposit(new BigDecimal(25).setScale(2));
        checkingAccount.setInterestRate(new BigDecimal(0).setScale(2));
        checkingAccount.setCompoundRate(CompoundRate.NA);
        checkingAccount = accountTypeDao.addAccountType(checkingAccount);

        List<AccountType> bank1AccountTypes = new ArrayList<>();
        bank1AccountTypes.add(savingsAccount);
        bank1AccountTypes.add(checkingAccount);

        List<AccountType> bank2AccountTypes = new ArrayList<>();
        bank2AccountTypes.add(savingsAccount);

        List<AccountType> bank3AccountTypes = new ArrayList<>();
        bank3AccountTypes.add(checkingAccount);

        Bank bank1 = new Bank();
        bank1.setName("RBC Royal Bank");
        bank1.setLocation("Canada");
        bank1.setAccountTypes(bank1AccountTypes);
        bank1 = bankDao.addBank(bank1);

        Bank bank2 = new Bank();
        bank2.setName("JPMorgan Chase & Co.");
        bank2.setLocation("United States");
        bank2.setAccountTypes(bank2AccountTypes);
        bank2 = bankDao.addBank(bank2);

        Bank bank3 = new Bank();
        bank3.setName("BNP Paribas");
        bank3.setLocation("France");
        bank3.setAccountTypes(bank3AccountTypes);
        bank3 = bankDao.addBank(bank3);

        List<Bank> banksFromDao = bankDao.getAllBanks();

        //Assert
        assertEquals(3, banksFromDao.size(),"There should be 3 banks.");
        assertTrue(banksFromDao.contains(bank1));
        assertTrue(banksFromDao.contains(bank2));
        assertTrue(banksFromDao.contains(bank3));
    }

    @Test
    @DisplayName("Update Bank")
    void updateBank() {
        //Create new account type : CHECKING
        AccountType checkingAccount = new AccountType();
        checkingAccount.setType(BankAccountType.CHECKING);
        checkingAccount.setMinimumStartDeposit(new BigDecimal(25).setScale(2));
        checkingAccount.setInterestRate(new BigDecimal(0).setScale(2));
        checkingAccount.setCompoundRate(CompoundRate.NA);
        checkingAccount = accountTypeDao.addAccountType(checkingAccount);

        List<AccountType> bankAccountTypes = new ArrayList<>();
        bankAccountTypes.add(checkingAccount);

        Bank bank = new Bank();
        bank.setName("RBC Royal Bank");
        bank.setLocation("Canada");
        bank.setAccountTypes(bankAccountTypes);
        bank = bankDao.addBank(bank);

        Bank bankFromDao = bankDao.getBankByID(bank.getBankID());
        assertEquals(bank, bankFromDao);

        //Update bank name & location
        bank.setName("Updated Bank Name");
        bank.setLocation("Relocated address");

        bankDao.updateBank(bank);

        assertNotEquals(bankDao, bankFromDao);

        //Now retrieve the modified bank
        bankFromDao = bankDao.getBankByID(bank.getBankID());

        //And assert they are equal
        assertEquals(bank, bankFromDao);
    }

    @Test
    void deleteBankByID() {
        //Create new account type : CHECKING
        AccountType checkingAccount = new AccountType();
        checkingAccount.setType(BankAccountType.CHECKING);
        checkingAccount.setMinimumStartDeposit(new BigDecimal(25).setScale(2));
        checkingAccount.setInterestRate(new BigDecimal(0).setScale(2));
        checkingAccount.setCompoundRate(CompoundRate.NA);
        checkingAccount = accountTypeDao.addAccountType(checkingAccount);

        List<AccountType> bankAccountTypes = new ArrayList<>();
        bankAccountTypes.add(checkingAccount);

        Bank bank = new Bank();
        bank.setName("RBC Royal Bank");
        bank.setLocation("Canada");
        bank.setAccountTypes(bankAccountTypes);
        bank = bankDao.addBank(bank);

        Bank bankFromDao = bankDao.getBankByID(bank.getBankID());
        assertEquals(bank, bankFromDao);

        //Now delete the bank
        bankDao.deleteBankByID(bank.getBankID());

        //Try to retrieve the deleted bank
        bankFromDao = bankDao.getBankByID(bank.getBankID());

        //Assert it is now null
        assertNull(bankFromDao);
    }

    @Test
    void getBanksByCustomer() {
    }

    @Test
    void getBankByAccount() {
    }
}