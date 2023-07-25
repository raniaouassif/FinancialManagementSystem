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
class AccountDaoDBTest {
    @Autowired
    AccountDao accountDao;

    @Autowired
    BankDao bankDao;

    @Autowired
    AccountTypeDao accountTypeDao;

    @Autowired
    CustomerDao customerDao;

    @Autowired
    TransactionDao transactionDao;

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
    @DisplayName("Get And Add Account ")
    void testGetAndAddAccountByID() {
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

        //Now create account
        Account account = new Account();
        account.setOpeningDate(LocalDate.now().minusYears(5));
        account.setCustomer(customer);
        account.setBank(bank);
        account.setAccountType(savingsAccount);

        //Call the add method from DAO
        account = accountDao.addAccount(account);

        //Create a few transactions
        Transaction transaction = new Transaction();
        transaction.setTo(account);
        transaction.setFrom(account);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAmount(new BigDecimal(5000.00).setScale(2, RoundingMode.HALF_UP));
        transaction.setDateTime(transactionDateTime);
        transaction = transactionDao.addTransaction(transaction);

        Transaction transaction2 = new Transaction();
        transaction2.setTo(account);
        transaction2.setFrom(account);
        transaction2.setTransactionType(TransactionType.WITHDRAW);
        transaction2.setAmount(new BigDecimal(100.00).setScale(2, RoundingMode.HALF_UP));
        transaction2.setDateTime(transactionDateTime.minusMonths(1));
        transaction2 = transactionDao.addTransaction(transaction2);

        //Call the get method from DAO
        Account accountFromDao = accountDao.getAccountByID(account.getAccountID());

        // Assert that the two objects have the same properties
        assertEquals(account.getAccountID(),  accountFromDao.getAccountID()); //accountID
        assertEquals(account.getOpeningDate(),  accountFromDao.getOpeningDate()); //openingDate
        assertEquals(account.getStatus(), accountFromDao.getStatus()); //status
        assertEquals(account.getDepositBalance(), accountFromDao.getDepositBalance()); //depositBalance
        assertEquals(account.getInterestBalance(), accountFromDao.getInterestBalance()); //interestBalance
        assertEquals(account.getTotalBalance(), accountFromDao.getTotalBalance()); //totalBalance
        assertEquals(account.getCustomer().getCustomerID(), accountFromDao.getCustomer().getCustomerID()); //customerID
        assertEquals(account.getBank().getBankID(), accountFromDao.getBank().getBankID()); //bankID
        assertEquals(account.getAccountType().getAccountTypeID(),  accountFromDao.getAccountType().getAccountTypeID()); //accountTypeID
        assertEquals(2, accountFromDao.getAccountTransactions().size()); //accountTransactions

    }

    @Test
    @DisplayName("Get All Accounts")
    void testGetAllAccounts() {
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
        customer.setFirstName("Rania");
        customer.setLastName("Ouassif");
        customer.setPhoneNumber("12-123-123");
        customer.setAccounts(new ArrayList<>());
        customer = customerDao.addCustomer(customer);

        //Now create account
        Account account = new Account();
        account.setOpeningDate(LocalDate.now().minusYears(5));
        account.setCustomer(customer);
        account.setBank(bank);
        account.setAccountType(savingsAccount);
        account = accountDao.addAccount(account);//Call the add method from DAO

        //Now create account
        Account account2 = new Account();
        account2.setOpeningDate(LocalDate.now().minusYears(5));
        account2.setCustomer(customer);
        account2.setBank(bank2);
        account2.setAccountType(savingsAccount);
        account2 = accountDao.addAccount(account2);//Call the add method from DAO

        //Call the getAll method from DAO
        List<Account> accountsFromDao = accountDao.getAllAccounts();

        //Assert
        assertEquals(2, accountsFromDao.size());
    }


    @Test
    @DisplayName("Update Account")
    void testUpdateAccount() {
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

        //Now create account
        Account account = new Account();
        account.setOpeningDate(LocalDate.now().minusYears(5));
        account.setCustomer(customer);
        account.setBank(bank);
        account.setAccountType(savingsAccount);

        //Call the add method from DAO
        account = accountDao.addAccount(account);

        //Call the get method from DAO
        Account accountFromDao = accountDao.getAccountByID(account.getAccountID());

        // Assert that the two objects have the same original properties
        assertEquals(account.getAccountID(),  accountFromDao.getAccountID()); //accountID
        assertEquals(account.getOpeningDate(),  accountFromDao.getOpeningDate()); //openingDate
        assertEquals(account.getClosingDate(),  accountFromDao.getClosingDate()); //closingDate
        assertEquals(account.getClosingReason(),  accountFromDao.getClosingReason()); //closingReason
        assertEquals(account.getStatus(), accountFromDao.getStatus()); //status
        assertEquals(account.getDepositBalance(), accountFromDao.getDepositBalance()); //depositBalance
        assertEquals(account.getInterestBalance(), accountFromDao.getInterestBalance()); //interestBalance
        assertEquals(account.getTotalBalance(), accountFromDao.getTotalBalance()); //totalBalance
        assertEquals(account.getCustomer().getCustomerID(), accountFromDao.getCustomer().getCustomerID()); //customerID
        assertEquals(account.getBank().getBankID(), accountFromDao.getBank().getBankID()); //bankID
        assertEquals(account.getAccountType().getAccountTypeID(),  accountFromDao.getAccountType().getAccountTypeID()); //accountTypeID

        //Now update the status to closed , the closing date and closing reason
        account.setStatus(AccountStatus.CLOSED);
        account.setClosingDate(LocalDate.now());
        account.setClosingReason("Switched to another bank.");

        //Update method
        accountDao.updateAccount(account);

        //Retrieve the updated object
        accountFromDao = accountDao.getAccountByID(account.getAccountID());

        //Assert that the two object have the same updated properties
        assertEquals(account.getStatus(), accountFromDao.getStatus()); //status
        assertEquals(account.getClosingDate(),  accountFromDao.getClosingDate()); //closingDate
        assertEquals(account.getClosingReason(),  accountFromDao.getClosingReason()); //closingReason

        //But that they also kept the other variables intact
        assertEquals(account.getAccountID(),  accountFromDao.getAccountID()); //accountID
        assertEquals(account.getOpeningDate(),  accountFromDao.getOpeningDate()); //openingDate
        assertEquals(account.getDepositBalance(), accountFromDao.getDepositBalance()); //depositBalance
        assertEquals(account.getInterestBalance(), accountFromDao.getInterestBalance()); //interestBalance
        assertEquals(account.getTotalBalance(), accountFromDao.getTotalBalance()); //totalBalance
        assertEquals(account.getCustomer().getCustomerID(), accountFromDao.getCustomer().getCustomerID()); //customerID
        assertEquals(account.getBank().getBankID(), accountFromDao.getBank().getBankID()); //bankID
        assertEquals(account.getAccountType().getAccountTypeID(),  accountFromDao.getAccountType().getAccountTypeID()); //accountTypeID
    }

    @Test
    @DisplayName("Delete Account By ID")
    void testDeleteAccountByID() {
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

        //Now create account
        Account account = new Account();
        account.setOpeningDate(LocalDate.now().minusYears(5));
        account.setCustomer(customer);
        account.setBank(bank);
        account.setAccountType(savingsAccount);

        //Call the add method from DAO
        account = accountDao.addAccount(account);

        //Call the get method from DAO
        Account accountFromDao = accountDao.getAccountByID(account.getAccountID());

        assertNotNull(accountFromDao);

        //Now delete
        accountDao.deleteAccountByID(account.getAccountID());

        //Try to retrieve the deleted object
        accountFromDao = accountDao.getAccountByID(account.getAccountID());

        //It should be null
        assertNull(accountFromDao);
    }

    @Test
    @DisplayName("Get Accounts By Customer")
    void testGetAccountsByCustomer() {
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
        customer.setFirstName("Jennifer");
        customer.setLastName("Anniston");
        customer.setPhoneNumber("12-123-123");
        customer.setAccounts(new ArrayList<>());
        customer = customerDao.addCustomer(customer);

        //Create a second customer
        Customer customer2 = new Customer();
        customer2.setFirstName("James");
        customer2.setLastName("Bond");
        customer2.setPhoneNumber("12-123-123");
        customer2.setAccounts(new ArrayList<>());
        customer2 = customerDao.addCustomer(customer2);

        //Now create James Bond first account
        Account account = new Account();
        account.setOpeningDate(LocalDate.now().minusYears(1));
        account.setCustomer(customer);
        account.setBank(bank);
        account.setAccountType(savingsAccount);
        account = accountDao.addAccount(account);//Call the add method from DAO

        //Now create James bond 2nd account in another Bank
        Account account2 = new Account();
        account2.setOpeningDate(LocalDate.now().minusMonths(5));
        account2.setCustomer(customer2);
        account2.setBank(bank2);
        account2.setAccountType(savingsAccount);
        account2 = accountDao.addAccount(account2);//Call the add method from DAO

        //Not Jennifer bond 2nd account in another Bank
        Account account3 = new Account();
        account3.setOpeningDate(LocalDate.now().minusMonths(5));
        account3.setCustomer(customer2);
        account3.setBank(bank2);
        account3.setAccountType(savingsAccount);
        account3 = accountDao.addAccount(account3);//Call the add method from DAO

        //Call the getAll method from DAO
        List<Account> accountsFromDao = accountDao.getAccountsByCustomer(customer2);

        //Assert that James bond has 2 accounts
        assertEquals(2, accountsFromDao.size());
    }

    @Test
    @DisplayName("Get Open Accounts By Customer")
    void testGetOpenAccountsByCustomer() {
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
        customer.setFirstName("Jennifer");
        customer.setLastName("Aniston");
        customer.setPhoneNumber("12-123-123");
        customer.setAccounts(new ArrayList<>());
        customer = customerDao.addCustomer(customer);

        //Create a second customer
        Customer customer2 = new Customer();
        customer2.setFirstName("James");
        customer2.setLastName("Bond");
        customer2.setPhoneNumber("12-123-123");
        customer2.setAccounts(new ArrayList<>());
        customer2 = customerDao.addCustomer(customer2);

        //Create Jennifer Aniston first account
        Account account = new Account();
        account.setOpeningDate(LocalDate.now().minusYears(1));
        account.setCustomer(customer);
        account.setBank(bank);
        account.setAccountType(savingsAccount);
        account = accountDao.addAccount(account);//Call the add method from DAO

        //Create James bond 2nd account in another Bank
        Account account2 = new Account();
        account2.setOpeningDate(LocalDate.now().minusMonths(5));
        account2.setCustomer(customer2);
        account2.setBank(bank2);
        account2.setAccountType(savingsAccount);
        account2 = accountDao.addAccount(account2);//Call the add method from DAO

        //Create James bond 2nd account
        Account account3 = new Account();
        account3.setOpeningDate(LocalDate.now().minusMonths(5));
        account3.setCustomer(customer2);
        account3.setBank(bank2);
        account3.setAccountType(savingsAccount);
        account3 = accountDao.addAccount(account3);//Call the add method from DAO

        //Create James bond 3rd account in another Bank
        Account account4 = new Account();
        account4.setOpeningDate(LocalDate.now().minusMonths(5));
        account4.setCustomer(customer2);
        account4.setBank(bank);
        account4.setAccountType(savingsAccount);
        account4 = accountDao.addAccount(account4);//Call the add method from DAO

        //Assert that all 3 accounts are automatically set to open
        List<Account> accountsFromDao = accountDao.getOpenAccountsByCustomer(customer2);

        //Assert that James bond has 3 OPEN accounts
        assertEquals(3, accountsFromDao.size());

        //Now CLOSE one of James Bond account
        //Set status to CLOSED , the closing date and closing reason
        account3.setStatus(AccountStatus.CLOSED);
        account3.setClosingDate(LocalDate.now());
        account3.setClosingReason("Switched to a better account type.");

        //Update & get method
        accountDao.updateAccount(account3);

        accountsFromDao = accountDao.getOpenAccountsByCustomer(customer2);

       //Assert that James Bond only has 2 OPEN accounts
        assertEquals(2, accountsFromDao.size());

    }
}