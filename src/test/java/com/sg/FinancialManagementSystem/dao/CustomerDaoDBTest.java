package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author raniaouassif on 2023-07-24
 */
@SpringBootTest
class CustomerDaoDBTest {
    @Autowired
    CustomerDao customerDao;

    @Autowired
    BankDao bankDao;

    @Autowired
    AccountDao accountDao;

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
        // Delete all accounts
        List<Account> accounts = accountDao.getAllAccounts();
        for(Account account : accounts) {
            accountDao.deleteAccountByID(account.getAccountID());
        }
        List<Customer> customerList = customerDao.getAllCustomers();
        for(Customer customer : customerList) {
            customerDao.deleteCustomerByID(customer.getCustomerID());
        }

    }

    @Test
    @DisplayName("Get And Add Customer")
    void testGetAndAddCustomerByID() {
        Customer customer = new Customer();
        customer.setFirstName("Lina");
        customer.setLastName("Williams");
        customer.setPhoneNumber("12-123-123");
        customer.setAccounts(new ArrayList<>());
        customer = customerDao.addCustomer(customer);

        Customer customerFromDao = customerDao.getCustomerByID(customer.getCustomerID());

        assertEquals(customer,customerFromDao);

    }

    @Test
    @DisplayName("Get All Customers")
    void testGetAllCustomers() {
        Customer customer = new Customer();
        customer.setFirstName("James");
        customer.setLastName("Bond");
        customer.setPhoneNumber("12-123-123");
        customer.setAccounts(new ArrayList<>());
        customer = customerDao.addCustomer(customer);

        Customer customer2 = new Customer();
        customer2.setFirstName("Rania");
        customer2.setLastName("Ouassif");
        customer2.setPhoneNumber("12-123-123");
        customer2.setAccounts(new ArrayList<>());
        customer2 = customerDao.addCustomer(customer);

        List<Customer> customerFromDao = customerDao.getAllCustomers();

        assertEquals(2,customerFromDao.size());
    }

    @Test
    @DisplayName("Update Customer")
    void testUpdateCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("James");
        customer.setLastName("Bond");
        customer.setPhoneNumber("12-123-123");
        customer.setAccounts(new ArrayList<>());
        customer = customerDao.addCustomer(customer);

        Customer customerFromDao = customerDao.getCustomerByID(customer.getCustomerID());

        assertEquals(customer,customerFromDao);

        //Now update the customer
        customer.setPhoneNumber("438-0923-213");
        customer.setLastName("BondUpdated");

        //Update the customer in the DAO
        customerDao.updateCustomer(customer);

        assertNotEquals(customer, customerFromDao);

        //Retrieve the updated customer
        customerFromDao =  customerDao.getCustomerByID(customer.getCustomerID());

        //Now they should be equal
        assertEquals(customer, customerFromDao);
    }

    @Test
    @DisplayName("Delete Customer By ID")
    void testDeleteCustomerByID() {
        Customer customer = new Customer();
        customer.setFirstName("James");
        customer.setLastName("Bond");
        customer.setPhoneNumber("12-123-123");
        customer.setAccounts(new ArrayList<>());
        customer = customerDao.addCustomer(customer);

        Customer customerFromDao = customerDao.getCustomerByID(customer.getCustomerID());

        assertEquals(customer,customerFromDao);

        //Now delete
        customerDao.deleteCustomerByID(customer.getCustomerID());

        //Try to retrieve the deleted customer
        customerFromDao = customerDao.getCustomerByID(customer.getCustomerID());

        assertNull(customerFromDao);
    }

    @Test
    void getCustomersByBank() {
        //Create new account type
        AccountType savingsAccount = new AccountType();
        savingsAccount.setType(BankAccountType.HIGH_INTEREST_SAVINGS);
        savingsAccount.setMinimumStartDeposit(new BigDecimal(100.00).setScale(2));
        savingsAccount.setInterestRate(new BigDecimal(3.5).setScale(2));
        savingsAccount.setCompoundRate(CompoundRate.QUARTERLY);
        savingsAccount = accountTypeDao.addAccountType(savingsAccount);

        List<AccountType> bankAccountTypes = new ArrayList<>();
        bankAccountTypes.add(savingsAccount);

        //Create new bank
        Bank bank = new Bank();
        bank.setName("RBC Royal Bank");
        bank.setLocation("Canada");
        bank.setAccountTypes(bankAccountTypes);
        bank = bankDao.addBank(bank);

        //Create another bank
        Bank bank2 = new Bank();
        bank2.setName("JPMorgan Chase & Co.");
        bank2.setLocation("United States");
        bank2.setAccountTypes(bankAccountTypes);
        bank2 = bankDao.addBank(bank2);

        //Create a new customer
        Customer customer = new Customer();
        customer.setFirstName("James");
        customer.setLastName("Bond");
        customer.setPhoneNumber("12-123-123");
        customer.setAccounts(new ArrayList<>());
        customer = customerDao.addCustomer(customer);

        //Create a new customer
        Customer customer2 = new Customer();
        customer2.setFirstName("James");
        customer2.setLastName("Bond");
        customer2.setPhoneNumber("438-098-1212");
        customer2.setAccounts(new ArrayList<>());
        customer2 = customerDao.addCustomer(customer);

        //Now create account
        // Customer is from required bank
        Account account = new Account();
        account.setOpeningDate(LocalDate.now().minusYears(5));
        account.setCustomer(customer);
        account.setBank(bank);
        account.setAccountType(savingsAccount);
        account.setDepositBalance(BigDecimal.valueOf(0));
        account = accountDao.addAccount(account);

        //Now create account
        // Customer is from required bank
        Account account2 = new Account();
        account2.setOpeningDate(LocalDate.now().minusWeeks(1));
        account2.setCustomer(customer);
        account2.setBank(bank2);
        account2.setAccountType(savingsAccount);
        account2.setDepositBalance(BigDecimal.valueOf(0));
        account2 = accountDao.addAccount(account2);

        //Now create account
        // Customer is from required bank
        Account account3 = new Account();
        account3.setOpeningDate(LocalDate.now().minusDays(5));
        account3.setCustomer(customer2);
        account3.setBank(bank2);
        account3.setAccountType(savingsAccount);
        account3.setDepositBalance(BigDecimal.valueOf(0));
        account3 = accountDao.addAccount(account3);

        List<Customer> customersBank1 = customerDao.getCustomersByBank(bank);

        List<Customer> customersBank2 = customerDao.getCustomersByBank(bank2);

        assertEquals(1, customersBank1.size());

        assertEquals(2, customersBank2.size());


    }

    @Test
    @DisplayName("Get Customer By Account Type")
    void testGetCustomersByAccountType() {
        //Create new account type: SAVINGS
        AccountType savingsAccount = new AccountType();
        savingsAccount.setType(BankAccountType.HIGH_INTEREST_SAVINGS);
        savingsAccount.setMinimumStartDeposit(new BigDecimal(100.00).setScale(2));
        savingsAccount.setInterestRate(new BigDecimal(3.5).setScale(2));
        savingsAccount.setCompoundRate(CompoundRate.QUARTERLY);
        savingsAccount = accountTypeDao.addAccountType(savingsAccount);

        //Create new account type : CHECKING
        AccountType checkingAccount = new AccountType();
        checkingAccount.setType(BankAccountType.CHECKING);
        checkingAccount.setMinimumStartDeposit(new BigDecimal(25).setScale(2, RoundingMode.HALF_UP));
        checkingAccount.setInterestRate(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP));
        checkingAccount.setCompoundRate(CompoundRate.NA);
        // Call the add method from dao
        checkingAccount = accountTypeDao.addAccountType(checkingAccount);

        List<AccountType> bankAccountTypes = new ArrayList<>();
        bankAccountTypes.add(savingsAccount);
        bankAccountTypes.add(checkingAccount);

        //Create new bank
        Bank bank = new Bank();
        bank.setName("RBC Royal Bank");
        bank.setLocation("Canada");
        bank.setAccountTypes(bankAccountTypes);
        bank = bankDao.addBank(bank);

        //Create a new customer
        Customer customer = new Customer();
        customer.setFirstName("Jane");
        customer.setLastName("Smith");
        customer.setPhoneNumber("12-123-123");
        customer.setAccounts(new ArrayList<>());
        customer = customerDao.addCustomer(customer);

        //Create a second customer
        Customer customer2 = new Customer();
        customer2.setFirstName("Michael");
        customer2.setLastName("Johnson");
        customer2.setPhoneNumber("438-098-1212");
        customer2.setAccounts(new ArrayList<>());
        customer2 = customerDao.addCustomer(customer);

        //Create a third customer
        Customer customer3 = new Customer();
        customer3.setFirstName("Robert");
        customer3.setLastName("Olivia");
        customer3.setPhoneNumber("438-098-1212");
        customer3.setAccounts(new ArrayList<>());
        customer3 = customerDao.addCustomer(customer);

        //Now create savingsAccount
        Account account = new Account();
        account.setOpeningDate(LocalDate.now().minusYears(5));
        account.setCustomer(customer);
        account.setBank(bank);
        account.setAccountType(savingsAccount);
        account.setDepositBalance(BigDecimal.valueOf(0));
        account = accountDao.addAccount(account);

        //Now create checkingAccount
        Account account2 = new Account();
        account2.setOpeningDate(LocalDate.now().minusWeeks(1));
        account2.setCustomer(customer3);
        account2.setBank(bank);
        account2.setAccountType(checkingAccount);
        account2.setDepositBalance(BigDecimal.valueOf(0));
        account2 = accountDao.addAccount(account2);

        //Now create savingsAccount
        // Customer is from required bank
        Account account3 = new Account();
        account3.setOpeningDate(LocalDate.now().minusDays(5));
        account3.setCustomer(customer2);
        account3.setBank(bank);
        account3.setAccountType(savingsAccount);
        account3.setDepositBalance(BigDecimal.valueOf(0));
        account3 = accountDao.addAccount(account3);

        List<Customer> customersChecking = customerDao.getCustomersByAccountType(checkingAccount);

        List<Customer> customersSavings = customerDao.getCustomersByAccountType(savingsAccount);

        assertEquals(1, customersChecking.size(),"Only 1 customer has a checking account");

        assertEquals(2, customersSavings.size(), "2 customers has a savings account");
    }

    @Test
    @DisplayName("Get Customer By Stock")
    void testGetCustomersByStock() {
    }


}