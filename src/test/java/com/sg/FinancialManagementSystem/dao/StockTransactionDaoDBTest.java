package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author raniaouassif on 2023-07-26
 */
@SpringBootTest
class StockTransactionDaoDBTest {
    @Autowired
    StockTransactionDao stockTransactionDao;
    @Autowired
    CompanyDao companyDao;
    @Autowired
    PortfolioDao portfolioDao;
    @Autowired
    ExchangeOrganizationDao eoDao;
    @Autowired
    StockDao stockDao;
    @Autowired
    CustomerDao customerDao;

    LocalDateTime transactionDateTime = LocalDateTime.now().withNano(0);
    @BeforeEach
    void setUp() {

        List<Customer> customerList = customerDao.getAllCustomers();
        for(Customer customer : customerList) {
            customerDao.deleteCustomerByID(customer.getCustomerID());
        }
        List<StockTransaction> stockTransactionList = stockTransactionDao.getAllStockTransactions();
        for(StockTransaction stockTransaction : stockTransactionList) {
            stockTransactionDao.deleteStockTransactionByID(stockTransaction.getStockTransactionID());
        }
        //Delete the stock
        List<Stock> stockList = stockDao.getAllStocks();
        for(Stock stock : stockList) {
            stockDao.deleteStockByID(stock.getStockID());
        }

        //Delete Companies
        List<Company> companyList = companyDao.getAllCompanies();
        for(Company company : companyList) {
            companyDao.deleteCompanyByID(company.getCompanyID());
        }

        //Delete all exchange organizations
        List<ExchangeOrganization> exchangeOrganizationList = eoDao.getAllExchangeOrganizations();
        for(ExchangeOrganization eo : exchangeOrganizationList) {
            eoDao.deleteExchangeOrganizationByID(eo.getExchangeOrganizationID());
        }
    }

    @Test
    @DisplayName("Get And Add Stock Transaction")
    void testGetAndAddStockTransaction() {
        // Create a customer and her portfolio
        Customer customer = new Customer();
        customer.setFirstName("Emily");
        customer.setLastName("Williams");
        customer.setPhoneNumber("12-123-123");
        customer = customerDao.addCustomer(customer);

        Portfolio portfolio = new Portfolio();
        portfolio.setCustomer(customer);
        portfolio.setPortfolioStocks(new ArrayList<>());
        portfolio.setStockTransactions(new ArrayList<>());
        portfolio = portfolioDao.addPortfolio(portfolio);

        // Create a company and its stock
        Company company = new Company();
        company.setName("Amazon");
        company.setIndustry("Multinational Technology Company");
        company.setStatus(CompanyStatus.PUBLIC);
        company.setRevenue(new BigDecimal(514.22).setScale(3, RoundingMode.HALF_UP));
        company.setProfit(new BigDecimal(225.152).setScale(3, RoundingMode.HALF_UP));
        company.setGrossMargin(new BigDecimal(41.8).setScale(3, RoundingMode.HALF_UP));
        company.setCashFlow(new BigDecimal(25.18).setScale(3, RoundingMode.HALF_UP));
        company = companyDao.addCompany(company); // add the company to the dao

        Stock stock = new Stock();
        stock.setTickerCode("AMZN");
        stock.setStatus(StockStatus.LISTED);
        stock.setSharePrice(new BigDecimal(3470.12).setScale(2,RoundingMode.HALF_UP));
        stock.setNumberOfOutstandingShares(2000000000L);
        stock.setMarketCap(new BigDecimal(6940240000000L).setScale(2, RoundingMode.HALF_UP));
        stock.setDailyVolume(1000000L);
        stock.setCompany(company);
        stock.setExchangeOrganizations(new ArrayList<>());
        stock = stockDao.addStock(stock); // add the stock to the dao

        List<Stock> stocks = new ArrayList<>();
        stocks.add(stock);

        ExchangeOrganization eo = new ExchangeOrganization();
        eo.setTickerCode("NASDAQ");
        eo.setName("NASDAQ Stock Market");
        eo.setStocks(stocks);
        eo = eoDao.addExchangeOrganization(eo); // Call the dao method

        // Now add a new stock transaction
        StockTransaction st = new StockTransaction();
        st.setStock(stock);
        st.setEo(eo);
        st.setPortfolio(portfolio);
        st.setDateTime(transactionDateTime);
        st.setType(StockTransactionType.BUY);
        st.setNumberOfShares(23);
        BigDecimal transactionCost = BigDecimal.valueOf(st.getNumberOfShares()).multiply(st.getStock().getSharePrice());
        st.setTransactionCost(transactionCost.setScale(2,RoundingMode.HALF_UP));
        st = stockTransactionDao.addStockTransaction(st);

        //Retrieve transaction
        StockTransaction stFromDao = stockTransactionDao.getStockTransactionByID(st.getStockTransactionID());

        //Assert they are the same:
        assertEquals(st.getStockTransactionID(),stFromDao.getStockTransactionID() );
        assertEquals(st.getPortfolio().getPortfolioID(), stFromDao.getPortfolio().getPortfolioID() );
        assertEquals(st.getStock().getStockID(), stFromDao.getStock().getStockID() );
        assertEquals(st.getDateTime(), stFromDao.getDateTime());
        assertEquals(st.getType(), stFromDao.getType());
        assertEquals(st.getNumberOfShares(),stFromDao.getNumberOfShares() );
        assertEquals(st.getTransactionCost(),stFromDao.getTransactionCost() );

    }

    @Test
    @DisplayName("Get All Transactions")
    void testGetAllStockTransactions() {
        // Create a customer and her portfolio
        Customer customer = new Customer();
        customer.setFirstName("Emily");
        customer.setLastName("Williams");
        customer.setPhoneNumber("12-123-123");
        customer = customerDao.addCustomer(customer);

        Portfolio portfolio = new Portfolio();
        portfolio.setCustomer(customer);
        portfolio.setPortfolioStocks(new ArrayList<>());
        portfolio.setStockTransactions(new ArrayList<>());
        portfolio = portfolioDao.addPortfolio(portfolio);

        // Create a company and its stock
        Company company = new Company();
        company.setName("Amazon");
        company.setIndustry("Multinational Technology Company");
        company.setStatus(CompanyStatus.PUBLIC);
        company.setRevenue(new BigDecimal(514.22).setScale(3, RoundingMode.HALF_UP));
        company.setProfit(new BigDecimal(225.152).setScale(3, RoundingMode.HALF_UP));
        company.setGrossMargin(new BigDecimal(41.8).setScale(3, RoundingMode.HALF_UP));
        company.setCashFlow(new BigDecimal(25.18).setScale(3, RoundingMode.HALF_UP));
        company = companyDao.addCompany(company); // add the company to the dao

        Stock stock = new Stock();
        stock.setTickerCode("AMZN");
        stock.setStatus(StockStatus.LISTED);
        stock.setSharePrice(new BigDecimal(3470.12).setScale(2,RoundingMode.HALF_UP));
        stock.setNumberOfOutstandingShares(2000000000L);
        stock.setMarketCap(new BigDecimal(6940240000000L).setScale(2, RoundingMode.HALF_UP));
        stock.setDailyVolume(1000000L);
        stock.setCompany(company);
        stock.setExchangeOrganizations(new ArrayList<>());
        stock = stockDao.addStock(stock); // add the stock to the dao

        List<Stock> stocks = new ArrayList<>();
        stocks.add(stock);

        ExchangeOrganization eo = new ExchangeOrganization();
        eo.setTickerCode("NASDAQ");
        eo.setName("NASDAQ Stock Market");
        eo.setStocks(stocks);
        eo = eoDao.addExchangeOrganization(eo); // Call the dao method

        // Add three new stock transaction
        StockTransaction st = new StockTransaction();
        st.setStock(stock);
        st.setEo(eo);
        st.setPortfolio(portfolio);
        st.setDateTime(transactionDateTime.minusMonths(1));
        st.setType(StockTransactionType.BUY);
        st.setNumberOfShares(23);
        BigDecimal transactionCost = BigDecimal.valueOf(st.getNumberOfShares()).multiply(st.getStock().getSharePrice());
        st.setTransactionCost(transactionCost.setScale(2,RoundingMode.HALF_UP));
        st = stockTransactionDao.addStockTransaction(st);

        // Now add a second stock transaction
        StockTransaction st2 = new StockTransaction();
        st2.setStock(stock);
        st2.setEo(eo);
        st2.setPortfolio(portfolio);
        st2.setDateTime(transactionDateTime.minusWeeks(1));
        st2.setType(StockTransactionType.SELL);
        st2.setNumberOfShares(5);
        BigDecimal transactionCost2 = BigDecimal.valueOf(st.getNumberOfShares()).multiply(st.getStock().getSharePrice());
        st2.setTransactionCost(transactionCost2.setScale(2,RoundingMode.HALF_UP));
        st2 = stockTransactionDao.addStockTransaction(st2);

        // Now add a third stock transaction
        StockTransaction st3 = new StockTransaction();
        st3.setStock(stock);
        st3.setEo(eo);
        st3.setPortfolio(portfolio);
        st3.setDateTime(transactionDateTime.minusDays(1));
        st3.setType(StockTransactionType.BUY);
        st3.setNumberOfShares(1);
        BigDecimal transactionCost3 = BigDecimal.valueOf(st.getNumberOfShares()).multiply(st.getStock().getSharePrice());
        st3.setTransactionCost(transactionCost3.setScale(2,RoundingMode.HALF_UP));
        st3 = stockTransactionDao.addStockTransaction(st3);


        List<StockTransaction> stockTransactionList = stockTransactionDao.getAllStockTransactions();

        assertEquals(3, stockTransactionList.size());
    }
    @Test
    @DisplayName("Update Stock Transaction")
    void testUpdateStockTransaction() {
        // Create a customer and her portfolio
        Customer customer = new Customer();
        customer.setFirstName("Emily");
        customer.setLastName("Williams");
        customer.setPhoneNumber("12-123-123");
        customer = customerDao.addCustomer(customer);

        Portfolio portfolio = new Portfolio();
        portfolio.setCustomer(customer);
        portfolio.setPortfolioStocks(new ArrayList<>());
        portfolio.setStockTransactions(new ArrayList<>());
        portfolio = portfolioDao.addPortfolio(portfolio);

        // Create a company and its stock
        Company company = new Company();
        company.setName("Amazon");
        company.setIndustry("Multinational Technology Company");
        company.setStatus(CompanyStatus.PUBLIC);
        company.setRevenue(new BigDecimal(514.22).setScale(3, RoundingMode.HALF_UP));
        company.setProfit(new BigDecimal(225.152).setScale(3, RoundingMode.HALF_UP));
        company.setGrossMargin(new BigDecimal(41.8).setScale(3, RoundingMode.HALF_UP));
        company.setCashFlow(new BigDecimal(25.18).setScale(3, RoundingMode.HALF_UP));
        company = companyDao.addCompany(company); // add the company to the dao

        Stock stock = new Stock();
        stock.setTickerCode("AMZN");
        stock.setStatus(StockStatus.LISTED);
        stock.setSharePrice(new BigDecimal(3470.12).setScale(2,RoundingMode.HALF_UP));
        stock.setNumberOfOutstandingShares(2000000000L);
        stock.setMarketCap(new BigDecimal(6940240000000L).setScale(2, RoundingMode.HALF_UP));
        stock.setDailyVolume(1000000L);
        stock.setCompany(company);
        stock.setExchangeOrganizations(new ArrayList<>());
        stock = stockDao.addStock(stock); // add the stock to the dao

        List<Stock> stocks = new ArrayList<>();
        stocks.add(stock);

        ExchangeOrganization eo = new ExchangeOrganization();
        eo.setTickerCode("NASDAQ");
        eo.setName("NASDAQ Stock Market");
        eo.setStocks(stocks);
        eo = eoDao.addExchangeOrganization(eo); // Call the dao method

        // Now add a new stock transaction
        StockTransaction st = new StockTransaction();
        st.setStock(stock);
        st.setEo(eo);
        st.setPortfolio(portfolio);
        st.setDateTime(transactionDateTime);
        st.setType(StockTransactionType.BUY);
        st.setNumberOfShares(23);
        BigDecimal transactionCost = BigDecimal.valueOf(st.getNumberOfShares()).multiply(st.getStock().getSharePrice());
        st.setTransactionCost(transactionCost.setScale(2,RoundingMode.HALF_UP));
        st = stockTransactionDao.addStockTransaction(st);

        //Retrieve transaction
        StockTransaction stFromDao = stockTransactionDao.getStockTransactionByID(st.getStockTransactionID());

        //Assert they are the same:
        assertEquals(st.getStockTransactionID(),stFromDao.getStockTransactionID() );
        assertEquals(st.getPortfolio().getPortfolioID(), stFromDao.getPortfolio().getPortfolioID() );
        assertEquals(st.getStock().getStockID(), stFromDao.getStock().getStockID() );
        assertEquals(st.getDateTime(), stFromDao.getDateTime());
        assertEquals(st.getType(), stFromDao.getType());
        assertEquals(st.getNumberOfShares(),stFromDao.getNumberOfShares() );
        assertEquals(st.getTransactionCost(),stFromDao.getTransactionCost() );

        //Now update the transaction number of shares and transaction cost
        st.setNumberOfShares(3);
        BigDecimal newtransactionCost = BigDecimal.valueOf(st.getNumberOfShares()).multiply(st.getStock().getSharePrice());
        st.setTransactionCost(newtransactionCost.setScale(2,RoundingMode.HALF_UP));
        st = stockTransactionDao.addStockTransaction(st);

        //Update using dao method
        stockTransactionDao.updateStockTransaction(st);

        //Retrieve updated from dao
        stFromDao =  stockTransactionDao.getStockTransactionByID(st.getStockTransactionID());
        //Assert they are the same:
        assertEquals(st.getStockTransactionID(),stFromDao.getStockTransactionID() );
        assertEquals(st.getPortfolio().getPortfolioID(), stFromDao.getPortfolio().getPortfolioID() );
        assertEquals(st.getStock().getStockID(), stFromDao.getStock().getStockID() );
        assertEquals(st.getDateTime(), stFromDao.getDateTime());
        assertEquals(st.getType(), stFromDao.getType());
        assertEquals(st.getNumberOfShares(),stFromDao.getNumberOfShares() );
        assertEquals(st.getTransactionCost(),stFromDao.getTransactionCost() );

    }

    @Test
    @DisplayName("Delete Stock Transaction By ID ")
    void testDeleteStockTransactionByID() {
        // Create a customer and her portfolio
        Customer customer = new Customer();
        customer.setFirstName("Emily");
        customer.setLastName("Williams");
        customer.setPhoneNumber("12-123-123");
        customer = customerDao.addCustomer(customer);

        Portfolio portfolio = new Portfolio();
        portfolio.setCustomer(customer);
        portfolio.setPortfolioStocks(new ArrayList<>());
        portfolio.setStockTransactions(new ArrayList<>());
        portfolio = portfolioDao.addPortfolio(portfolio);

        // Create a company and its stock
        Company company = new Company();
        company.setName("Amazon");
        company.setIndustry("Multinational Technology Company");
        company.setStatus(CompanyStatus.PUBLIC);
        company.setRevenue(new BigDecimal(514.22).setScale(3, RoundingMode.HALF_UP));
        company.setProfit(new BigDecimal(225.152).setScale(3, RoundingMode.HALF_UP));
        company.setGrossMargin(new BigDecimal(41.8).setScale(3, RoundingMode.HALF_UP));
        company.setCashFlow(new BigDecimal(25.18).setScale(3, RoundingMode.HALF_UP));
        company = companyDao.addCompany(company); // add the company to the dao

        Stock stock = new Stock();
        stock.setTickerCode("AMZN");
        stock.setStatus(StockStatus.LISTED);
        stock.setSharePrice(new BigDecimal(3470.12).setScale(2,RoundingMode.HALF_UP));
        stock.setNumberOfOutstandingShares(2000000000L);
        stock.setMarketCap(new BigDecimal(6940240000000L).setScale(2, RoundingMode.HALF_UP));
        stock.setDailyVolume(1000000L);
        stock.setCompany(company);
        stock.setExchangeOrganizations(new ArrayList<>());
        stock = stockDao.addStock(stock); // add the stock to the dao

        List<Stock> stocks = new ArrayList<>();
        stocks.add(stock);

        ExchangeOrganization eo = new ExchangeOrganization();
        eo.setTickerCode("NASDAQ");
        eo.setName("NASDAQ Stock Market");
        eo.setStocks(stocks);
        eo = eoDao.addExchangeOrganization(eo); // Call the dao method

        // Now add a new stock transaction
        StockTransaction st = new StockTransaction();
        st.setStock(stock);
        st.setEo(eo);
        st.setPortfolio(portfolio);
        st.setDateTime(transactionDateTime);
        st.setType(StockTransactionType.BUY);
        st.setNumberOfShares(23);
        BigDecimal transactionCost = BigDecimal.valueOf(st.getNumberOfShares()).multiply(st.getStock().getSharePrice());
        st.setTransactionCost(transactionCost.setScale(2,RoundingMode.HALF_UP));
        st = stockTransactionDao.addStockTransaction(st);

        //Retrieve transaction
        StockTransaction stFromDao = stockTransactionDao.getStockTransactionByID(st.getStockTransactionID());

        assertNotNull(stFromDao);

        //Now delete the transaction
        stockTransactionDao.deleteStockTransactionByID(st.getStockTransactionID());

        //Try to retrieve deleted stock transaction
        stFromDao = stockTransactionDao.getStockTransactionByID(st.getStockTransactionID());

        //Assert it is null
        assertNull(stFromDao);
    }

    @Test
    @DisplayName("Get Stock Transactions By Portfolio")
    void testGetStockTransactionsByPortfolio() {
        // Create a customer and her portfolio
        Customer customer = new Customer();
        customer.setFirstName("Emily");
        customer.setLastName("Williams");
        customer.setPhoneNumber("12-123-123");
        customer = customerDao.addCustomer(customer);

        Portfolio portfolio = new Portfolio();
        portfolio.setCustomer(customer);
        portfolio.setPortfolioStocks(new ArrayList<>());
        portfolio.setStockTransactions(new ArrayList<>());
        portfolio = portfolioDao.addPortfolio(portfolio);

        // Create a second customer and his portfolio
        Customer customer2 = new Customer();
        customer2.setFirstName("Robert");
        customer2.setLastName("Williams");
        customer2.setPhoneNumber("12-123-6543");
        customer2 = customerDao.addCustomer(customer2);

        Portfolio portfolio2 = new Portfolio();
        portfolio2.setCustomer(customer2);
        portfolio2.setPortfolioStocks(new ArrayList<>());
        portfolio2.setStockTransactions(new ArrayList<>());
        portfolio2 = portfolioDao.addPortfolio(portfolio2);

        // Create a company and its stock
        Company company = new Company();
        company.setName("Amazon");
        company.setIndustry("Multinational Technology Company");
        company.setStatus(CompanyStatus.PUBLIC);
        company.setRevenue(new BigDecimal(514.22).setScale(3, RoundingMode.HALF_UP));
        company.setProfit(new BigDecimal(225.152).setScale(3, RoundingMode.HALF_UP));
        company.setGrossMargin(new BigDecimal(41.8).setScale(3, RoundingMode.HALF_UP));
        company.setCashFlow(new BigDecimal(25.18).setScale(3, RoundingMode.HALF_UP));
        company = companyDao.addCompany(company); // add the company to the dao

        Stock stock = new Stock();
        stock.setTickerCode("AMZN");
        stock.setStatus(StockStatus.LISTED);
        stock.setSharePrice(new BigDecimal(3470.12).setScale(2,RoundingMode.HALF_UP));
        stock.setNumberOfOutstandingShares(2000000000L);
        stock.setMarketCap(new BigDecimal(6940240000000L).setScale(2, RoundingMode.HALF_UP));
        stock.setDailyVolume(1000000L);
        stock.setCompany(company);
        stock.setExchangeOrganizations(new ArrayList<>());
        stock = stockDao.addStock(stock); // add the stock to the dao

        List<Stock> stocks = new ArrayList<>();
        stocks.add(stock);

        ExchangeOrganization eo = new ExchangeOrganization();
        eo.setTickerCode("NASDAQ");
        eo.setName("NASDAQ Stock Market");
        eo.setStocks(stocks);
        eo = eoDao.addExchangeOrganization(eo); // Call the dao method

        // Add three new stock transaction

        StockTransaction st = new StockTransaction();
        st.setStock(stock);
        st.setEo(eo);
        st.setPortfolio(portfolio);
        st.setDateTime(transactionDateTime.minusMonths(1));
        st.setType(StockTransactionType.BUY);
        st.setNumberOfShares(23);
        BigDecimal transactionCost = BigDecimal.valueOf(st.getNumberOfShares()).multiply(st.getStock().getSharePrice());
        st.setTransactionCost(transactionCost.setScale(2,RoundingMode.HALF_UP));
        st = stockTransactionDao.addStockTransaction(st);

        // Now add a second stock transaction
        StockTransaction st2 = new StockTransaction();
        st2.setStock(stock);
        st2.setEo(eo);
        st2.setPortfolio(portfolio);
        st2.setDateTime(transactionDateTime.minusWeeks(1));
        st2.setType(StockTransactionType.SELL);
        st2.setNumberOfShares(5);
        BigDecimal transactionCost2 = BigDecimal.valueOf(st.getNumberOfShares()).multiply(st.getStock().getSharePrice());
        st2.setTransactionCost(transactionCost2.setScale(2,RoundingMode.HALF_UP));
        st2 = stockTransactionDao.addStockTransaction(st2);

        // Now add a third stock transaction
        StockTransaction st3 = new StockTransaction();
        st3.setStock(stock);
        st3.setEo(eo);
        st3.setPortfolio(portfolio2);
        st3.setDateTime(transactionDateTime.minusDays(1));
        st3.setType(StockTransactionType.BUY);
        st3.setNumberOfShares(1);
        BigDecimal transactionCost3 = BigDecimal.valueOf(st.getNumberOfShares()).multiply(st.getStock().getSharePrice());
        st3.setTransactionCost(transactionCost3.setScale(2,RoundingMode.HALF_UP));
        st3 = stockTransactionDao.addStockTransaction(st3);

        List<StockTransaction> stockTransactionsPortfolio = stockTransactionDao.getStockTransactionsByPortfolio(portfolio);
        List<StockTransaction> stockTransactionsPortfolio2 = stockTransactionDao.getStockTransactionsByPortfolio(portfolio2);

        assertEquals(2, stockTransactionsPortfolio.size());
        assertEquals(1, stockTransactionsPortfolio2.size());

    }
    @Test
    @DisplayName("Get Stock Transactions By Stock")
    void getStockTransactionByStock() {
        // Create a customer and her portfolio
        Customer customer = new Customer();
        customer.setFirstName("Emily");
        customer.setLastName("Williams");
        customer.setPhoneNumber("12-123-123");
        customer = customerDao.addCustomer(customer);

        Portfolio portfolio = new Portfolio();
        portfolio.setCustomer(customer);
        portfolio.setPortfolioStocks(new ArrayList<>());
        portfolio.setStockTransactions(new ArrayList<>());
        portfolio = portfolioDao.addPortfolio(portfolio);

        // Create a company and its stock
        Company company = new Company();
        company.setName("Amazon");
        company.setIndustry("Multinational Technology Company");
        company.setStatus(CompanyStatus.PUBLIC);
        company.setRevenue(new BigDecimal(514.22).setScale(3, RoundingMode.HALF_UP));
        company.setProfit(new BigDecimal(225.152).setScale(3, RoundingMode.HALF_UP));
        company.setGrossMargin(new BigDecimal(41.8).setScale(3, RoundingMode.HALF_UP));
        company.setCashFlow(new BigDecimal(25.18).setScale(3, RoundingMode.HALF_UP));
        company = companyDao.addCompany(company); // add the company to the dao

        Stock stock = new Stock();
        stock.setTickerCode("AMZN");
        stock.setStatus(StockStatus.LISTED);
        stock.setSharePrice(new BigDecimal(3470.12).setScale(2,RoundingMode.HALF_UP));
        stock.setNumberOfOutstandingShares(2000000000L);
        stock.setMarketCap(new BigDecimal(6940240000000L).setScale(2, RoundingMode.HALF_UP));
        stock.setDailyVolume(1000000L);
        stock.setCompany(company);
        stock.setExchangeOrganizations(new ArrayList<>());
        stock = stockDao.addStock(stock); // add the stock to the dao

        Company company2 = new Company();
        company2.setName("Microsoft");
        company2.setIndustry("Multinational Technology Company");
        company2.setStatus(CompanyStatus.PUBLIC);
        company2.setRevenue(new BigDecimal(689.45).setScale(3, RoundingMode.HALF_UP));
        company2.setProfit(new BigDecimal(315.72).setScale(3, RoundingMode.HALF_UP));
        company2.setGrossMargin(new BigDecimal(56.8).setScale(3, RoundingMode.HALF_UP));
        company2.setCashFlow(new BigDecimal(40.91).setScale(3, RoundingMode.HALF_UP));
        company2 = companyDao.addCompany(company2); // add the company to the dao

        Stock stock2 = new Stock();
        stock2.setTickerCode("MSFT");
        stock2.setStatus(StockStatus.LISTED);
        stock2.setSharePrice(new BigDecimal(365.29).setScale(2, RoundingMode.HALF_UP));
        stock2.setNumberOfOutstandingShares(8000000000L);
        stock2.setMarketCap(new BigDecimal(2922320000000L).setScale(2, RoundingMode.HALF_UP));
        stock2.setDailyVolume(750000L);
        stock2.setCompany(company2);
        stock2.setExchangeOrganizations(new ArrayList<>());
        stock2 = stockDao.addStock(stock2); // add the stock to the dao

        List<Stock> stocks = new ArrayList<>();
        stocks.add(stock);
        stocks.add(stock2);

        ExchangeOrganization eo = new ExchangeOrganization();
        eo.setTickerCode("NASDAQ");
        eo.setName("NASDAQ Stock Market");
        eo.setStocks(stocks);
        eo = eoDao.addExchangeOrganization(eo); // Call the dao method

        // Add three new stock transaction

        StockTransaction st = new StockTransaction();
        st.setStock(stock);
        st.setEo(eo);
        st.setPortfolio(portfolio);
        st.setDateTime(transactionDateTime.minusMonths(1));
        st.setType(StockTransactionType.BUY);
        st.setNumberOfShares(23);
        BigDecimal transactionCost = BigDecimal.valueOf(st.getNumberOfShares()).multiply(st.getStock().getSharePrice());
        st.setTransactionCost(transactionCost.setScale(2,RoundingMode.HALF_UP));
        st = stockTransactionDao.addStockTransaction(st);

        // Now add a second stock transaction
        StockTransaction st2 = new StockTransaction();
        st2.setStock(stock2);
        st2.setEo(eo);
        st2.setPortfolio(portfolio);
        st2.setDateTime(transactionDateTime.minusWeeks(1));
        st2.setType(StockTransactionType.SELL);
        st2.setNumberOfShares(5);
        BigDecimal transactionCost2 = BigDecimal.valueOf(st.getNumberOfShares()).multiply(st.getStock().getSharePrice());
        st2.setTransactionCost(transactionCost2.setScale(2,RoundingMode.HALF_UP));
        st2 = stockTransactionDao.addStockTransaction(st2);

        // Now add a third stock transaction
        StockTransaction st3 = new StockTransaction();
        st3.setStock(stock2);
        st3.setEo(eo);
        st3.setPortfolio(portfolio);
        st3.setDateTime(transactionDateTime.minusDays(1));
        st3.setType(StockTransactionType.BUY);
        st3.setNumberOfShares(1);
        BigDecimal transactionCost3 = BigDecimal.valueOf(st.getNumberOfShares()).multiply(st.getStock().getSharePrice());
        st3.setTransactionCost(transactionCost3.setScale(2,RoundingMode.HALF_UP));
        st3 = stockTransactionDao.addStockTransaction(st3);

        List<StockTransaction> stockTransactionsForStock1 = stockTransactionDao.getStockTransactionByStock(stock);
        List<StockTransaction> stockTransactionsForStock2 = stockTransactionDao.getStockTransactionByStock(stock2);

        assertEquals(1, stockTransactionsForStock1.size());
        assertEquals(2, stockTransactionsForStock2.size());
    }
}