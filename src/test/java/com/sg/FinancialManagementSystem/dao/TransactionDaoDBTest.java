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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author raniaouassif on 2023-07-24
 */

@SpringBootTest
class TransactionDaoDBTest {

    @Autowired
    TransactionDao transactionDao;
    @Autowired
    AccountDao accountDao;
    @Autowired
    BankDao bankDao;
    @Autowired
    AccountTypeDao accountTypeDao;
    @Autowired
    CustomerDao customerDao;

    LocalDateTime transactionDateTime = LocalDateTime.now().withNano(0);
    @BeforeEach
    void setUp() {
        // Delete all account types
        List<AccountType> accountTypes = accountTypeDao.getAllAccountTypes();
        for(AccountType accountType : accountTypes) {
            accountTypeDao.deleteAccountTypeByID(accountType.getAccountTypeID());
        }
        // Delete all banks
        List<Bank> banks = bankDao.getAllBanks();
        for(Bank bank : banks) {
            bankDao.deleteBankByID(bank.getBankID());
        }

        //Delete all transactions
        List<Transaction>  transactions = transactionDao.getAllTransactions();
        for(Transaction transaction : transactions) {
            transactionDao.deleteTransactionByID(transaction.getTransactionID());
        }

        // Delete all accounts
        List<Account> accounts = accountDao.getAllAccounts();
        for(Account account : accounts) {
            accountDao.deleteAccountByID(account.getAccountID());
        }

        //Then delete the customers
        List<Customer> customers = customerDao.getAllCustomers();
        for(Customer customer : customers) {
            customerDao.deleteCustomerByID(customer.getCustomerID());
        }
    }

    @Test
    @DisplayName("Get And Add Transaction")
    void testGetTransactionByID() {
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

        //Create a new customer
        Customer customer = new Customer();
        customer.setFirstName("Rania");
        customer.setLastName("Ouassif");
        customer.setPhoneNumber("12-123-123");
        customer.setAccounts(new ArrayList<>());
        customer = customerDao.addCustomer(customer);

        //Create account
        Account account = new Account();
        account.setOpeningDate(LocalDate.now().minusYears(5));
        account.setCustomer(customer);
        account.setBank(bank);
        account.setAccountType(savingsAccount);
        account = accountDao.addAccount(account);

        //Create a transaction
        Transaction transaction = new Transaction();
        transaction.setTo(account);
        transaction.setFrom(account);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAmount(new BigDecimal(5000.00).setScale(2, RoundingMode.HALF_UP));
        transaction.setDateTime(transactionDateTime);

        //Call the add method from the dao
        transaction = transactionDao.addTransaction(transaction);

        //Retrieve the transaction
        Transaction transactionFromDao = transactionDao.getTransactionByID(transaction.getTransactionID());

        // Assert that they have the same props
        assertEquals(transaction.getTransactionID(),transactionFromDao.getTransactionID());
        assertEquals(transaction.getTransactionType(),transactionFromDao.getTransactionType());
        assertEquals(transaction.getDateTime(),transactionFromDao.getDateTime());
        assertEquals(transaction.getFrom().getAccountID(), transactionFromDao.getFrom().getAccountID());
        assertEquals(transaction.getTo().getAccountID(), transactionFromDao.getTo().getAccountID());
        assertEquals(transaction.getAmount(),transactionFromDao.getAmount());

    }

    @Test
    @DisplayName("Get All Transactions")
    void getAllTransactions() {
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

        //Create a new customer
        Customer customer = new Customer();
        customer.setFirstName("Rania");
        customer.setLastName("Ouassif");
        customer.setPhoneNumber("12-123-123");
        customer.setAccounts(new ArrayList<>());
        customer = customerDao.addCustomer(customer);

        //Create account
        Account account = new Account();
        account.setOpeningDate(LocalDate.now().minusYears(5));
        account.setCustomer(customer);
        account.setBank(bank);
        account.setAccountType(savingsAccount);
        account = accountDao.addAccount(account);

        //Create transactions
        Transaction transaction = new Transaction();
        transaction.setTo(account);
        transaction.setFrom(account);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAmount(new BigDecimal(1000.00).setScale(2, RoundingMode.HALF_UP));
        transaction.setDateTime(transactionDateTime);
        transaction = transactionDao.addTransaction(transaction);

        Transaction transaction2 = new Transaction();
        transaction2.setTo(account);
        transaction2.setFrom(account);
        transaction2.setTransactionType(TransactionType.WITHDRAW);
        transaction2.setAmount(new BigDecimal(100.00).setScale(2, RoundingMode.HALF_UP));
        transaction2.setDateTime(transactionDateTime);
        transaction2 = transactionDao.addTransaction(transaction2);

        Transaction transaction3 = new Transaction();
        transaction3.setTo(account);
        transaction3.setFrom(account);
        transaction3.setTransactionType(TransactionType.DEPOSIT);
        transaction3.setAmount(new BigDecimal(100.00).setScale(2, RoundingMode.HALF_UP));
        transaction3.setDateTime(transactionDateTime);
        transaction3 = transactionDao.addTransaction(transaction3);

        //Retrieve the transaction
        List<Transaction> transactionsFromDao = transactionDao.getAllTransactions();

        assertEquals(3, transactionsFromDao.size(), "There should be 3 transactions");
    }

    @Test
    @DisplayName("Delete Transaction By ID")
    void testDeleteTransactionByID() {
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

        //Create a new customer
        Customer customer = new Customer();
        customer.setFirstName("Rania");
        customer.setLastName("Ouassif");
        customer.setPhoneNumber("12-123-123");
        customer.setAccounts(new ArrayList<>());
        customer = customerDao.addCustomer(customer);

        //Create account
        Account account = new Account();
        account.setOpeningDate(LocalDate.now().minusYears(5));
        account.setCustomer(customer);
        account.setBank(bank);
        account.setAccountType(savingsAccount);
        account = accountDao.addAccount(account);

        //Create a transaction
        Transaction transaction = new Transaction();
        transaction.setTo(account);
        transaction.setFrom(account);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAmount(new BigDecimal(100.00).setScale(2, RoundingMode.HALF_UP));
        transaction.setDateTime(transactionDateTime);

        //Call the add method from the dao
        transaction = transactionDao.addTransaction(transaction);

        //Retrieve the transaction
        Transaction transactionFromDao = transactionDao.getTransactionByID(transaction.getTransactionID());

        // Assert that the retrieved object is not null
        assertNotNull(transactionFromDao);

        //Now delete the transaction
        transactionDao.deleteTransactionByID(transaction.getTransactionID());

        //Now try to retrieve the deleted object again
        transactionFromDao = transactionDao.getTransactionByID(transaction.getTransactionID());

        //Assert that the object has been succesfully deleted
        assertNull(transactionFromDao);
    }
    @Test
    @DisplayName("Get Transaction By Account")
    void testGetTransactionsByAccount() {
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

        //Create a new customer
        Customer customer = new Customer();
        customer.setFirstName("Rania");
        customer.setLastName("Ouassif");
        customer.setPhoneNumber("12-123-123");
        customer.setAccounts(new ArrayList<>());
        customer = customerDao.addCustomer(customer);

        //Create account
        Account account = new Account();
        account.setOpeningDate(LocalDate.now().minusYears(5));
        account.setCustomer(customer);
        account.setBank(bank);
        account.setAccountType(savingsAccount);
        account = accountDao.addAccount(account);

        //Create another account
        Account account2 = new Account();
        account2.setOpeningDate(LocalDate.now().minusYears(5));
        account2.setCustomer(customer);
        account2.setBank(bank);
        account2.setAccountType(savingsAccount);
        account2 = accountDao.addAccount(account2);

        //Create transactions
        Transaction transaction = new Transaction();
        transaction.setTo(account);
        transaction.setFrom(account);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAmount(new BigDecimal(100.00).setScale(2, RoundingMode.HALF_UP));
        transaction.setDateTime(transactionDateTime);
        transaction = transactionDao.addTransaction(transaction);

        Transaction transaction2 = new Transaction();
        transaction2.setTo(account);
        transaction2.setFrom(account);
        transaction2.setTransactionType(TransactionType.WITHDRAW);
        transaction2.setAmount(new BigDecimal(50.00).setScale(2, RoundingMode.HALF_UP));
        transaction2.setDateTime(transactionDateTime);
        transaction2 = transactionDao.addTransaction(transaction2);

        Transaction transaction3 = new Transaction();
        transaction3.setTo(account2);
        transaction3.setFrom(account2);
        transaction3.setTransactionType(TransactionType.WITHDRAW);
        transaction3.setAmount(new BigDecimal(100.00).setScale(2, RoundingMode.HALF_UP));
        transaction3.setDateTime(transactionDateTime);
        transaction3 = transactionDao.addTransaction(transaction3);

        Transaction transaction4 = new Transaction();
        transaction4.setFrom(account2);
        transaction4.setTo(account);
        transaction4.setTransactionType(TransactionType.TRANSFER);
        transaction4.setAmount(new BigDecimal(100.00).setScale(2, RoundingMode.HALF_UP));
        transaction4.setDateTime(transactionDateTime);
        transaction4 = transactionDao.addTransaction(transaction4);

        //Retrieve the transaction by account ID
        List<Transaction> transactionsFromDaoAccount1 = transactionDao.getTransactionsByAccount(account);

        assertEquals(3, transactionsFromDaoAccount1.size(), "There should be 3 transactions for this account");

        //Retrieve the transaction by account ID
        List<Transaction> transactionsFromDaoAccount2 = transactionDao.getTransactionsByAccount(account2);

        assertEquals(2, transactionsFromDaoAccount2.size(), "There should be 3 transactions for this account");


    }
}