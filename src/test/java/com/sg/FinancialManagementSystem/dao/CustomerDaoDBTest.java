package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

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


    @Test
    @DisplayName("Get And Add Customer")
    void testGetAndAddCustomerByID() {
        Customer customer = new Customer();
        customer.setFirstName("Rania");
        customer.setLastName("Ouassif");
        customer.setPhoneNumber("12-123-123");
        customer.setAccounts(new ArrayList<>());
        customer = customerDao.addCustomer(customer);

        Customer customerFromDao = customerDao.getCustomerByID(customer.getCustomerID());

        assertEquals(customer,customerFromDao);

    }

    @Test
    void getAllCustomers() {
    }

    @Test
    void addCustomer() {
    }

    @Test
    void updateCustomer() {
    }

    @Test
    void deleteCustomerByID() {
    }

    @Test
    void getCustomersByBank() {
    }

    @Test
    void getCustomersByAccountType() {
    }

    @Test
    void getCustomersByStock() {
    }
}