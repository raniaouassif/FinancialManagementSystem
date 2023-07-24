package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dao.CustomerDao;
import com.sg.FinancialManagementSystem.dao.mappers.CustomerMapper;
import com.sg.FinancialManagementSystem.dto.AccountType;
import com.sg.FinancialManagementSystem.dto.Bank;
import com.sg.FinancialManagementSystem.dto.Customer;
import com.sg.FinancialManagementSystem.dto.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
            return jdbcTemplate.queryForObject(GET_CUSTOMER_BY_ID, new CustomerMapper(), customerID);
        } catch(DataAccessException e) {
            System.out.println("CustomerDaoDB: getCustomerByID() failed");
            return null;
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        final String GET_ALL_CUSTOMERS = "SELECT * FROM Customer;";
        return jdbcTemplate.query(GET_ALL_CUSTOMERS, new CustomerMapper());
    }

    @Override
    public Customer addCustomer(Customer customer) {
        final String ADD_CUSTOMER = "INSERT INTO Customer (firstName, lastName, phoneNumber) VALUES (?,?,?)";

        jdbcTemplate.update(ADD_CUSTOMER, customer.getFirstName(), customer.getLastName(), customer.getPhoneNumber());

        int newID = jdbcTemplate.update("SELECT LAST_INSERT_ID()", Integer.class);
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
        //TODO
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
}
