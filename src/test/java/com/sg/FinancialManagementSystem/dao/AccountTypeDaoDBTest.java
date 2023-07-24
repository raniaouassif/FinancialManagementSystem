package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.AccountType;
import com.sg.FinancialManagementSystem.dto.BankAccountType;
import com.sg.FinancialManagementSystem.dto.CompoundRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author raniaouassif on 2023-07-24
 */
@SpringBootTest
class AccountTypeDaoDBTest {

    @Autowired
    AccountTypeDao accountTypeDao;
    @BeforeEach
    void setUp() {
        List<AccountType> accountTypes = accountTypeDao.getAllAccountTypes();
        for(AccountType accountType : accountTypes) {
            accountTypeDao.deleteAccountTypeByID(accountType.getAccountTypeID());
        }
    }

    @Test
    @DisplayName("Get And Add Account Type")
    void testGetAndAddAccountTypeByID() {
        //Create new account type : SAVING
        AccountType savingsAccount = new AccountType();
        savingsAccount.setType(BankAccountType.HIGH_INTEREST_SAVINGS);
        savingsAccount.setMinimumStartDeposit(new BigDecimal(100.00).setScale(2));
        savingsAccount.setInterestRate(new BigDecimal(3.5).setScale(2));
        savingsAccount.setCompoundRate(CompoundRate.QUARTERLY);
        // Call the add method from dao
        savingsAccount = accountTypeDao.addAccountType(savingsAccount);

        // Retrieve the account type from dao
        AccountType savingsAccountFromDao = accountTypeDao.getAccountTypeByID(savingsAccount.getAccountTypeID());

        // Assert both objects are equal
        assertEquals(savingsAccount, savingsAccountFromDao);

        //Create new account type : CHECKING
        AccountType checkingAccount = new AccountType();
        checkingAccount.setType(BankAccountType.CHECKING);
        checkingAccount.setMinimumStartDeposit(new BigDecimal(25).setScale(2));
        checkingAccount.setInterestRate(new BigDecimal(0).setScale(2));
        checkingAccount.setCompoundRate(CompoundRate.NA);
        // Call the add method from dao
        checkingAccount = accountTypeDao.addAccountType(checkingAccount);

        // Retrieve the account type from dao
        AccountType checkingAccountFromDao = accountTypeDao.getAccountTypeByID(checkingAccount.getAccountTypeID());

        // Assert both objects are equal
        assertEquals(checkingAccount, checkingAccountFromDao);
    }

    @Test
    @DisplayName("Get All Account Types")
    void testGetAllAccountTypes() {
        //Create new account type : CHECKING
        AccountType checkingAccount = new AccountType();
        checkingAccount.setType(BankAccountType.CHECKING);
        checkingAccount.setMinimumStartDeposit(new BigDecimal(25).setScale(2, RoundingMode.HALF_UP));
        checkingAccount.setInterestRate(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP));
        checkingAccount.setCompoundRate(CompoundRate.NA);
        // Call the add method from dao
        checkingAccount = accountTypeDao.addAccountType(checkingAccount);

        //Create new account type : HIGH_INTEREST_SAVING
        AccountType highSavingsAccount = new AccountType();
        highSavingsAccount.setType(BankAccountType.HIGH_INTEREST_SAVINGS);
        highSavingsAccount.setMinimumStartDeposit(new BigDecimal(100.00).setScale(2, RoundingMode.HALF_UP));
        highSavingsAccount.setInterestRate(new BigDecimal(3.5).setScale(2, RoundingMode.HALF_UP));
        highSavingsAccount.setCompoundRate(CompoundRate.QUARTERLY);
        // Call the add method from dao
        highSavingsAccount = accountTypeDao.addAccountType(highSavingsAccount);

        //Create new account type : LOW_INTEREST_SAVING
        AccountType lowSavingsAccount = new AccountType();
        lowSavingsAccount.setType(BankAccountType.LOW_INTEREST_SAVINGS);
        lowSavingsAccount.setMinimumStartDeposit(new BigDecimal(50).setScale(2, RoundingMode.HALF_UP));
        lowSavingsAccount.setInterestRate(new BigDecimal(0.1).setScale(2, RoundingMode.HALF_UP));
        lowSavingsAccount.setCompoundRate(CompoundRate.ANNUALLY);
        // Call the add method from dao
        lowSavingsAccount = accountTypeDao.addAccountType(lowSavingsAccount);

        List<AccountType> accountTypesFromDao = accountTypeDao.getAllAccountTypes();

        assertEquals(3, accountTypesFromDao.size(), "There should be 3 account types returned from the DAO.");
        assertTrue(accountTypesFromDao.contains(checkingAccount), "The DAO should contain the checking account.");
        assertTrue(accountTypesFromDao.contains(highSavingsAccount), "The DAO should contain the high savings account.");
        assertTrue(accountTypesFromDao.contains(lowSavingsAccount), "The DAO should contain the low savings account.");

    }

    @Test
    @DisplayName("Update Account Type")
    void testUpdateAccountType() {
        //Create new account type : CHECKING
        AccountType checkingAccount = new AccountType();
        checkingAccount.setType(BankAccountType.CHECKING);
        checkingAccount.setMinimumStartDeposit(new BigDecimal(25).setScale(2));
        checkingAccount.setInterestRate(new BigDecimal(0).setScale(2));
        checkingAccount.setCompoundRate(CompoundRate.NA);
        // Call the add method from dao
        checkingAccount = accountTypeDao.addAccountType(checkingAccount);

        // Retrieve the account type from dao
        AccountType checkingAccountFromDao = accountTypeDao.getAccountTypeByID(checkingAccount.getAccountTypeID());

        // Assert both objects are equal
        assertEquals(checkingAccount, checkingAccountFromDao);

        //Now update the checkingAccount
        checkingAccount.setMinimumStartDeposit(new BigDecimal(200).setScale(2));

        accountTypeDao.updateAccountType(checkingAccount); //Call updateAccountType dao method
        assertNotEquals(checkingAccount, checkingAccountFromDao);

        checkingAccountFromDao = accountTypeDao.getAccountTypeByID(checkingAccount.getAccountTypeID());
        assertEquals(checkingAccount, checkingAccountFromDao);
    }

    @Test
    @DisplayName("Delete Account Type By ID")
    void testDeleteAccountTypeByID() {
        //Create new account type : CHECKING
        AccountType checkingAccount = new AccountType();
        checkingAccount.setType(BankAccountType.CHECKING);
        checkingAccount.setMinimumStartDeposit(new BigDecimal(25).setScale(2));
        checkingAccount.setInterestRate(new BigDecimal(0).setScale(2));
        checkingAccount.setCompoundRate(CompoundRate.NA);
        // Call the add method from dao
        checkingAccount = accountTypeDao.addAccountType(checkingAccount);

        // Retrieve the account type from dao
        AccountType checkingAccountFromDao = accountTypeDao.getAccountTypeByID(checkingAccount.getAccountTypeID());

        // Assert both objects are equal
        assertEquals(checkingAccount, checkingAccountFromDao);

        //Now delete the object
        accountTypeDao.deleteAccountTypeByID(checkingAccount.getAccountTypeID());

        //Try to retrieve the deleted account type
        checkingAccountFromDao = accountTypeDao.getAccountTypeByID(checkingAccount.getAccountTypeID());
        //Assert the account type has been deleted
        assertNull(checkingAccountFromDao);
    }

    @Test
    void getAccountTypesByBank() {
    }

    @Test
    void getAccountTypesByCustomer() {
    }

    @Test
    void getAccountTypeByAccount() {
    }
}