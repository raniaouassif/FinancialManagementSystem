package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.Customer;
import com.sg.FinancialManagementSystem.dto.Portfolio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author raniaouassif on 2023-07-26
 */
@SpringBootTest
class PortfolioDaoDBTest {

    @Autowired
    PortfolioDao portfolioDao;

    @Autowired
    CustomerDao customerDao;

    @BeforeEach
    void setUp() {
        //First delete the portfolios
        List<Portfolio> portfolios = portfolioDao.getAllPortfolios();
        for(Portfolio portfolio : portfolios) {
            portfolioDao.deletePortfolioByID(portfolio.getPortfolioID());
        }
        //Then the customers
        List<Customer> customerList = customerDao.getAllCustomers();
        for(Customer customer : customerList) {
            customerDao.deleteCustomerByID(customer.getCustomerID());
        }
    }

    @Test
    @DisplayName("Get And Add Portfolio")
    void testGetAndAddPortfolioByID() {
        Customer customer = new Customer();
        customer.setFirstName("Emily");
        customer.setLastName("Williams");
        customer.setPhoneNumber("12-123-123");
        customer = customerDao.addCustomer(customer);

        Portfolio portfolio = new Portfolio();
        portfolio.setCustomer(customer);
        portfolio.setPortfolioStocks(new ArrayList<>());
        portfolio.setStockTransactions(new ArrayList<>());
        portfolio.setStockTransactions(new ArrayList<>());
        portfolio = portfolioDao.addPortfolio(portfolio);


        //Retrieve portfolio from DAO
        Portfolio portfolioFromDao = portfolioDao.getPortfolioByID(portfolio.getPortfolioID());

        //Assert they are the same
        assertEquals(portfolio, portfolioFromDao);

    }

    @Test
    @DisplayName("Get All Portfolios")
    void testGetAllPortfolios() {
        //Create two customers with their respective portfolios
        Customer customer = new Customer();
        customer.setFirstName("Emily");
        customer.setLastName("Williams");
        customer.setPhoneNumber("12-123-123");
        customer = customerDao.addCustomer(customer);

        Portfolio portfolio = new Portfolio();
        portfolio.setCustomer(customer);
        portfolio.setPortfolioStocks(new ArrayList<>());
        portfolio.setStockTransactions(new ArrayList<>());
        portfolio.setStockTransactions(new ArrayList<>());
        portfolio = portfolioDao.addPortfolio(portfolio);

        Customer customer2 = new Customer();
        customer2.setFirstName("Jason");
        customer2.setLastName("Robert");
        customer2.setPhoneNumber("12-123-123");
        customer2 = customerDao.addCustomer(customer2);

        Portfolio portfolio2 = new Portfolio();
        portfolio2.setCustomer(customer2);
        portfolio2.setPortfolioStocks(new ArrayList<>());
        portfolio2.setStockTransactions(new ArrayList<>());
        portfolio2.setStockTransactions(new ArrayList<>());
        portfolio2 = portfolioDao.addPortfolio(portfolio2);

        //Retrieve all portfolios from DAO
        List<Portfolio> portfoliosFromDao = portfolioDao.getAllPortfolios();

        //Assert there are 2 portfolios
        assertEquals(2, portfoliosFromDao.size());
        assertTrue(portfoliosFromDao.contains(portfolio));
        assertTrue(portfoliosFromDao.contains(portfolio2));

    }

    @Test
    @DisplayName("Update Portfolio")
    void testUpdatePortfolio() {
        Customer customer = new Customer();
        customer.setFirstName("Emily");
        customer.setLastName("Williams");
        customer.setPhoneNumber("12-123-123");
        customer = customerDao.addCustomer(customer);

        Portfolio portfolio = new Portfolio();
        portfolio.setCustomer(customer);
        portfolio.setPortfolioStocks(new ArrayList<>());
        portfolio.setStockTransactions(new ArrayList<>());
        portfolio.setStockTransactions(new ArrayList<>());
        portfolio = portfolioDao.addPortfolio(portfolio);

        //Retrieve portfolio from DAO
        Portfolio portfolioFromDao = portfolioDao.getPortfolioByID(portfolio.getPortfolioID());

        //Assert they are the same
        assertEquals(portfolio, portfolioFromDao);

        // Now update the portfolio balance
        portfolio.setBalance(new BigDecimal(5000).setScale(2, RoundingMode.HALF_UP));

        //Update the portfolio in DAO
        portfolioDao.updatePortfolio(portfolio);

        assertNotEquals(portfolio, portfolioFromDao);

        //Retrieve the updated portfolio
        portfolioFromDao =  portfolioDao.getPortfolioByID(portfolio.getPortfolioID());
        //Assert they are the same
        assertEquals(portfolio, portfolioFromDao);
    }

    @Test
    @DisplayName("Delete Portfolio By ID")
    void testDeletePortfolioByID() {
        Customer customer = new Customer();
        customer.setFirstName("Emily");
        customer.setLastName("Williams");
        customer.setPhoneNumber("12-123-123");
        customer = customerDao.addCustomer(customer);

        Portfolio portfolio = new Portfolio();
        portfolio.setCustomer(customer);
        portfolio.setPortfolioStocks(new ArrayList<>());
        portfolio.setStockTransactions(new ArrayList<>());
        portfolio.setStockTransactions(new ArrayList<>());
        portfolio = portfolioDao.addPortfolio(portfolio);

        //Retrieve portfolio from DAO
        Portfolio portfolioFromDao = portfolioDao.getPortfolioByID(portfolio.getPortfolioID());

        //Assert they are the same
        assertNotNull(portfolioFromDao);

        //Now delete the portfolio
        portfolioDao.deletePortfolioByID(portfolio.getPortfolioID());

        //Try to retrieve the deleted portfolio
        portfolioFromDao = portfolioDao.getPortfolioByID(portfolio.getPortfolioID());

        //It should be null now since deleted
        assertNull(portfolioFromDao);

    }
}