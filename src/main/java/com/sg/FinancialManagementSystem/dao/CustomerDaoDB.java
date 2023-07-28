package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dao.CustomerDao;
import com.sg.FinancialManagementSystem.dao.mappers.*;
import com.sg.FinancialManagementSystem.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-24
 */
@Repository
public class CustomerDaoDB implements CustomerDao {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public Customer getCustomerByID(int customerID) {
        try {
            final String GET_CUSTOMER_BY_ID = "SELECT * FROM Customer WHERE customerID = ?";
            Customer customer = jdbcTemplate.queryForObject(GET_CUSTOMER_BY_ID, new CustomerMapper(), customerID);
            setPortfolioAndAccountsByCustomer(customer);
            return  customer;
        } catch(DataAccessException e) {
            System.out.println("CustomerDaoDB: getCustomerByID() failed");
            return null;
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        final String GET_ALL_CUSTOMERS = "SELECT * FROM Customer;";
        List<Customer> customers = jdbcTemplate.query(GET_ALL_CUSTOMERS, new CustomerMapper());
        setPortfolioAndAccountsByCustomersList(customers);
        return customers;
    }

    @Override
    public Customer addCustomer(Customer customer) {
        final String ADD_CUSTOMER = "INSERT INTO Customer (firstName, lastName, phoneNumber) VALUES (?,?,?)";

        jdbcTemplate.update(ADD_CUSTOMER, customer.getFirstName(), customer.getLastName(), customer.getPhoneNumber());

        int newID = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        customer.setCustomerID(newID);
        return customer;
    }

    @Override
    public void updateCustomer(Customer customer) {
        System.out.println(customer.getFirstName() + customer.getLastName())
        ;System.out.println(customer.getPhoneNumber());

        System.out.println(customer.getCustomerID());
        final String UPDATE_CUSTOMER = "UPDATE Customer SET firstName = ?, lastName = ?, phoneNumber = ? WHERE customerID = ?";
        jdbcTemplate.update(
                UPDATE_CUSTOMER,
                customer.getFirstName(),
                customer.getLastName(),
                customer.getPhoneNumber(),
                customer.getCustomerID());

        Customer updated = getCustomerByID(customer.getCustomerID());
    }

    @Override
    public void deleteCustomerByID(int customerID) {
        // First delete the portfolioBridge
        final String DELETE_PORTFOLIO_BRIDGE = "DELETE pb FROM PortfolioBridge pb " +
                "JOIN Portfolio p ON p.portfolioID = pb.portfolioID " +
                "JOIN Customer c ON c.customerID = p.customerID " +
                "WHERE c.customerID = ?";
        jdbcTemplate.update(DELETE_PORTFOLIO_BRIDGE, customerID);

        //Then delete the portfolio stock
        final String DELETE_PORTFOLIO_STOCK = "DELETE ps FROM PortfolioStock ps " +
                "JOIN Portfolio p ON p.portfolioID = ps.portfolioID " +
                "JOIN Customer c ON c.customerID = p.customerID " +
                "WHERE c.customerID = ?";
        jdbcTemplate.update(DELETE_PORTFOLIO_STOCK, customerID);

        //Then delete the portfolio
        final String DELETE_PORTFOLIO = "DELETE FROM Portfolio WHERE customerID = ?";
        jdbcTemplate.update(DELETE_PORTFOLIO, customerID);

        // DELETE ACCOUNT TRANSACTION
        final String DELETE_ACCOUNT_TRANSACTION = "DELETE at FROM AccountTransaction at " +
                "JOIN Account a ON a.accountID = at.accountID1 OR a.accountID = at.accountID2 " +
                "JOIN Customer c ON c.customerID = a.customerID " +
                "WHERE c.customerID = ?";
        jdbcTemplate.update(DELETE_ACCOUNT_TRANSACTION, customerID);

        //DELETE THE ACCOUNT BRIDGE
        final String DELETE_ACCOUNT_BRIDGE = "DELETE ab FROM AccountBridge ab " +
                "JOIN Account a ON a.accountID = ab.accountId " +
                "JOIN Customer c ON c.customerID = a.customerID " +
                "WHERE c.customerID = ?";
        jdbcTemplate.update(DELETE_ACCOUNT_BRIDGE, customerID);

        //DELETE THE ACCOUNT
        final String DELETE_ACCOUNT = "DELETE a FROM Account a " +
                "JOIN Customer c ON c.customerID = a.customerID " +
                "WHERE c.customerID = ?";
        jdbcTemplate.update(DELETE_ACCOUNT, customerID);

        //Then delete the customer
        final String DELETE_CUSTOMER_BY_ID = "DELETE FROM Customer WHERE customerID = ?";
        jdbcTemplate.update(DELETE_CUSTOMER_BY_ID, customerID);
    }

    @Override
    public List<Customer> getCustomersByBank(Bank bank) {
        final String GET_CUSTOMERS_BY_BANK = "SELECT  c.* " +
                "FROM Customer c " +
                "JOIN Account a ON a.customerID = c.customerID " +
                "JOIN AccountBridge ba ON ba.accountID = a.accountID " +
                "JOIN BankAccountType bat ON bat.bankID = ba.bankID AND bat.accountTypeID = ba.accountTypeID " +
                "JOIN Bank b ON b.bankID = bat.bankID " +
                "WHERE b.bankID = ?;";

        List<Customer> customers = jdbcTemplate.query(GET_CUSTOMERS_BY_BANK, new CustomerMapper(), bank.getBankID());
        setPortfolioAndAccountsByCustomersList(customers);

        return customers.size() == 0 ? new ArrayList<>() : customers;
    }

    @Override
    public List<Customer> getCustomersByAccountType(AccountType accountType) {
        final String GET_CUSTOMERS_BY_ACCOUNT_TYPE = "SELECT c.* FROM Customer c " +
                "JOIN Account a ON a.customerID = c.customerID " +
                "JOIN AccountBridge ba ON ba.accountID = a.accountID " +
                "JOIN BankAccountType bat ON bat.bankID = ba.bankID AND bat.accountTypeID = ba.accountTypeID " +
                "JOIN AccountType at ON at.accountTypeID = bat.accountTypeID " +
                "WHERE at.accountTypeID = ?";

        List<Customer> customers = jdbcTemplate.query(GET_CUSTOMERS_BY_ACCOUNT_TYPE, new CustomerMapper(), accountType.getAccountTypeID());
        setPortfolioAndAccountsByCustomersList(customers);


        return customers.size() == 0 ? new ArrayList<>() : customers;
    }

    @Override
    public List<Customer> getCustomersByStock(Stock stock) {
        final String GET_CUSTOMERS_BY_STOCK = "SELECT c.* FROM Customer c " +
                "JOIN Portfolio p ON p.customerID = c.customerID " +
                "JOIN PortfolioStock ps ON ps.portfolioID = p.portfolioID " +
                "JOIN Stock s ON s.stockID = ps.stockID " +
                "WHERE s.stockID = ?";

        List<Customer> customers = jdbcTemplate.query(GET_CUSTOMERS_BY_STOCK, new CustomerMapper(), stock.getStockID());
        setPortfolioAndAccountsByCustomersList(customers);
        return customers;
    }

    //PRIVATE HELPER FUNCTIONS
    private List<Account> getAccountsByCustomer(int customerID) {
        final String GET_ACCOUNTS_BY_CUSTOMER = "SELECT a.* FROM Account a " +
                "JOIN Customer c ON c.customerID = a.customerID " +
                "WHERE c.customerID = ?";

        List<Account> accounts = jdbcTemplate.query(GET_ACCOUNTS_BY_CUSTOMER, new AccountMapper(), customerID);

        //Set the fields for each account
        setAccountTypeAndBankAndTransactionsForAccountList(accounts);

        return accounts.size() == 0 ? new ArrayList<>() : accounts;
    }

    private Portfolio getPortfolioByCustomer(Customer customer) {
        try {
            final String GET_PORTFOLIO_BY_CUSTOMER = "SELECT * FROM Portfolio WHERE customerID = ?";
            return jdbcTemplate.queryForObject(GET_PORTFOLIO_BY_CUSTOMER, new PortfolioMapper(), customer.getCustomerID());
        } catch (DataAccessException e){
            System.out.println("CustomerDaoDB: getPortfolioByCustomer() failed.");
            return null;
        }
    }

    //Set portfolio and accounts by customer
    private void setPortfolioAndAccountsByCustomer(Customer customer) {
        customer.setPortfolio(getPortfolioByCustomer(customer));
        customer.setAccounts(getAccountsByCustomer(customer.getCustomerID()));
    }

    // Set portfolio and accounts for a list of customers
    private void setPortfolioAndAccountsByCustomersList(List<Customer> customersList) {
        for(Customer customer : customersList) {
            setPortfolioAndAccountsByCustomer(customer);
        }
    }

    //FROM ACCOUNT
    //Set account type, bank and list of transactions for a given account
    private void setAccountTypeAndBankAndTransactionsForAccount(Account account) {
        account.setAccountType(getAccountTypeByAccount(account)); // Set the account Type
        account.setBank(getBankByAccount(account));  // Set the bank
        account.setAccountTransactions(getAccountTransactionsByAccount(account)); // Set the list of transactions
    }

    //Set account type, bank and list of transactions for a list of accounts
    private void setAccountTypeAndBankAndTransactionsForAccountList(List<Account> accountList ){
        for(Account account : accountList) {
            setAccountTypeAndBankAndTransactionsForAccount(account);
        }
    }

    private Bank getBankByAccount(Account account) {
        try {
            final String GET_BANK_BY_ACCOUNT = "SELECT b.* FROM Bank b " +
                    "JOIN BankAccountType bat ON bat.bankID = b.bankID " +
                    "JOIN AccountBridge ba ON ba.bankID = bat.bankID AND ba.accountTypeID = bat.accountTypeID " +
                    "JOIN Account a ON a.accountID = ba.accountID " +
                    "WHERE a.accountID = ?";

            // TODO : ANYTHING TO SET ?

            return jdbcTemplate.queryForObject(GET_BANK_BY_ACCOUNT, new BankMapper(), account.getAccountID());
        } catch (DataAccessException e) {
            return null;
        }
    }

    private AccountType getAccountTypeByAccount(Account account) {
        try {
            final String GET_ACCOUNT_TYPE_BY_ACCOUNT = "SELECT at.* FROM AccountType at " +
                    "JOIN BankAccountType bat ON bat.accountTypeID = at.accountTypeID " +
                    "JOIN AccountBridge ba ON ba.accountTypeID = bat.accountTypeID AND ba.bankID = bat.bankID " +
                    "JOIN Account a ON a.accountID = ba.accountID " +
                    "WHERE a.accountID = ?";

            AccountType retrievedAccountType = jdbcTemplate.queryForObject(
                    GET_ACCOUNT_TYPE_BY_ACCOUNT,
                    new AccountTypeMapper(),
                    account.getAccountID()
            );

            // TODO : ANYTHING TO SET ?
            return retrievedAccountType;
        } catch (DataAccessException e) {
            return null;
        }
    }

    private List<Transaction> getAccountTransactionsByAccount(Account account) {
        //todo
        final String GET_ACCOUNT_TRANSACTIONS_BY_ACCOUNT = "SELECT t.* FROM Transaction t " +
                "JOIN AccountTransaction at ON at.transactionID = t.transactionID " +
                "JOIN Account a ON a.accountID = at.accountID1 OR a.accountID = at.accountID2 " +
                "WHERE a.accountID = ?";

        List<Transaction> retrievedTransactions = jdbcTemplate.query(
                GET_ACCOUNT_TRANSACTIONS_BY_ACCOUNT,
                new TransactionMapper(),
                account.getAccountID()
        );
        //TODO SET THE THINGS FOR TRANSACTION
        return retrievedTransactions.size() == 0 ? new ArrayList<>() : retrievedTransactions;
    }

}
