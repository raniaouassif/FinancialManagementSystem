package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dao.CustomerDao;
import com.sg.FinancialManagementSystem.dao.mappers.AccountMapper;
import com.sg.FinancialManagementSystem.dao.mappers.CustomerMapper;
import com.sg.FinancialManagementSystem.dao.mappers.PortfolioMapper;
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
        final String UPDATE_CUSTOMER = "UPDATE Customer SET firstName = ?, lastName = ?, phoneNumber = ? WHERE customerID = ?";
        jdbcTemplate.update(UPDATE_CUSTOMER, customer.getFirstName(), customer.getLastName(), customer.getPhoneNumber());
    }

    @Override
    public void deleteCustomerByID(int customerID) {
        Customer customerToDelete = getCustomerByID(customerID);
        String customerName = customerToDelete.getFirstName() + " " + customerToDelete.getLastName();
        // First set all the customer's accounts to closed and set customerID to null
        List<Account> customerAccounts = getAccountsByCustomer(customerID);

        final String UPDATE_ACCOUNTS_TO_CLOSED = "UPDATE Account SET "
                + "depositBalance = 0, "
                + "interestBalance = 0, "
                + "totalBalance = 0, "
                + "closingDate = ?, "
                + "status = 'CLOSED', "
                + "customerID = null, "
                + "closingReason = ? "
                + "WHERE accountID = ?;";

        String closingReason = "Customer " + customerName + " has been deleted from the system.";
        LocalDate closingDate = LocalDate.now();
        for(Account account : customerAccounts) {
            jdbcTemplate.update(UPDATE_ACCOUNTS_TO_CLOSED,
                    closingDate,
                    closingReason,
                    account.getAccountID());
        }

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

        List<Customer> retrievedCustomers = jdbcTemplate.query(GET_CUSTOMERS_BY_BANK, new CustomerMapper(), bank.getBankID());

        return retrievedCustomers.size() == 0 ? new ArrayList<>() : retrievedCustomers;
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
        return customers.size() == 0 ? new ArrayList<>() : customers;
    }

    @Override
    public List<Customer> getCustomersByStock(Stock stock) {
        //TODO
        return null;
    }

    //PRIVATE HELPER FUNCTIONS
    private List<Account> getAccountsByCustomer(int customerID) {
        final String GET_ACCOUNTS_BY_CUSTOMER = "SELECT a.* FROM Account a " +
                "JOIN Customer c ON c.customerID = a.customerID " +
                "WHERE c.customerID = ?";

        List<Account> accounts = jdbcTemplate.query(GET_ACCOUNTS_BY_CUSTOMER, new AccountMapper(), customerID);

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
}
