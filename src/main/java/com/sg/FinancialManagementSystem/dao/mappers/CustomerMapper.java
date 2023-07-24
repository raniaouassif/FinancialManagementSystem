package com.sg.FinancialManagementSystem.dao.mappers;

import com.sg.FinancialManagementSystem.dto.Account;
import com.sg.FinancialManagementSystem.dto.Customer;
import com.sg.FinancialManagementSystem.dto.Portfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
@Component
public class CustomerMapper implements RowMapper<Customer> {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerID(rs.getInt("customerID"));
        customer.setFirstName(rs.getString("firstName"));
        customer.setLastName(rs.getString("lastName"));
        customer.setPhoneNumber(rs.getString("phoneNumber"));
        customer.setAccounts(getAccountsByCustomer(customer));
        customer.setPortfolio(getPortfolioByCustomer(customer));
        return customer;
    }

    //PRIVATE HELPER FUNCTIONS

    private List<Account> getAccountsByCustomer(Customer customer) {
        final String GET_ACCOUNTS_BY_CUSTOMER = "SELECT * FROM Account a "
                + "WHERE customerID = ?";

        List<Account> retrievedAccounts = jdbcTemplate.query(
                GET_ACCOUNTS_BY_CUSTOMER,
                new AccountMapper(),
                customer.getCustomerID()
        );

        return retrievedAccounts.size() == 0 ? new ArrayList<>() : retrievedAccounts;
    }

    private Portfolio getPortfolioByCustomer(Customer customer) {
        try {
            final String GET_PORTFOLIO_BY_CUSTOMER = "SELECT * FROM Portfolio WHERE customerID = ?";
            return jdbcTemplate.queryForObject(GET_PORTFOLIO_BY_CUSTOMER, new PortfolioMapper(), customer.getCustomerID());
        } catch (DataAccessException e) {
            System.out.println("CustomerMapper: getPortfolioByCustomer() failed.");
            return null;
        }
    }
}
