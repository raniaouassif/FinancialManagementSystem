package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.*;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
public interface CustomerDao {

    Customer getCustomerByID(int customerID);

    List<Customer> getAllCustomers();

    Customer addCustomer(Customer customer);

    void updateCustomer(Customer customer);

    void deleteCustomerByID(int customerID);

    List<Customer> getCustomersByBank(Bank bank);

    List<Customer> getCustomersByAccountType(AccountType accountType);

    List<Customer> getCustomersByStock(Stock stock);
}
