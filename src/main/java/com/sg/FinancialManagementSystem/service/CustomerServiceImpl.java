package com.sg.FinancialManagementSystem.service;

import com.sg.FinancialManagementSystem.dao.CustomerDao;
import com.sg.FinancialManagementSystem.dto.AccountType;
import com.sg.FinancialManagementSystem.dto.Bank;
import com.sg.FinancialManagementSystem.dto.Customer;
import com.sg.FinancialManagementSystem.dto.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-26
 */
@Service
public class CustomerServiceImpl implements CustomerServiceInterface{
    @Autowired
    CustomerDao customerDao;
    @Override
    public Customer getCustomerByID(int customerID) {
        return customerDao.getCustomerByID(customerID);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDao.getAllCustomers();
    }

    @Override
    public Customer addCustomer(Customer customer) {
        return customerDao.addCustomer(customer);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customerDao.updateCustomer(customer);
    }

    @Override
    public void deleteCustomerByID(int customerID) {
        customerDao.deleteCustomerByID(customerID);
    }

    @Override
    public List<Customer> getCustomersByBank(Bank bank) {
        return customerDao.getCustomersByBank(bank);
    }

    @Override
    public List<Customer> getCustomersByAccountType(AccountType accountType) {
        return customerDao.getCustomersByAccountType(accountType);
    }

    @Override
    public List<Customer> getCustomersByStock(Stock stock) {
        return customerDao.getCustomersByStock(stock);
    }
}
