package com.sg.FinancialManagementSystem.service;

import com.sg.FinancialManagementSystem.dto.AccountType;
import com.sg.FinancialManagementSystem.dto.Bank;
import com.sg.FinancialManagementSystem.dto.Customer;
import com.sg.FinancialManagementSystem.dto.Stock;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-26
 */
public interface CustomerService {
    Customer getCustomerByID(int customerID);

    List<Customer> getAllCustomers();

    Customer addCustomer(Customer customer);

    void updateCustomer(Customer customer);

    void deleteCustomerByID(int customerID);

    List<Customer> getCustomersByBank(Bank bank);

    List<Customer> getCustomersByAccountType(AccountType accountType);

    List<Customer> getCustomersByStock(Stock stock);
}
