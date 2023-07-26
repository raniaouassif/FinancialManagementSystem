package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.*;
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
 * @author raniaouassif on 2023-07-25
 */
@SpringBootTest
class ExchangeOrganizationDaoDBTest {
    @Autowired
    CompanyDao companyDao;
    @Autowired
    StockDao stockDao;
    @Autowired
    ExchangeOrganizationDao eoDao;

    @Autowired
    AccountTypeDao accountTypeDao;
    @BeforeEach
    void setUp() {
        List<AccountType> accountTypes = accountTypeDao.getAllAccountTypes();
        for(AccountType accountType : accountTypes) {
            accountTypeDao.deleteAccountTypeByID(accountType.getAccountTypeID());
        }
        //Delete all exchange organizations
        List<ExchangeOrganization> exchangeOrganizationList = eoDao.getAllExchangeOrganizations();
        for(ExchangeOrganization eo : exchangeOrganizationList) {
            eoDao.deleteExchangeOrganizationByID(eo.getExchangeOrganizationID());
        }
    }

    @Test
    @DisplayName("Get And Add Exchange Organization")
    void testGetAndAddExchangeOrganizationByID() {
        ExchangeOrganization eo = new ExchangeOrganization();
        eo.setTickerCode("NASDAQ");
        eo.setName("NASDAQ Stock Market");
        eo.setStocks(new ArrayList<>());
        eo = eoDao.addExchangeOrganization(eo); // Call the dao method

        //Retrieve the eo using the get dao method
        ExchangeOrganization eoFromDao = eoDao.getExchangeOrganizationByID(eo.getExchangeOrganizationID());

        //Assert both are equal
        assertEquals(eo, eoFromDao);
    }

    @Test
    @DisplayName("Get All Exchange Organizations")
    void testGetAllExchangeOrganizations() {
        ExchangeOrganization eo = new ExchangeOrganization();
        eo.setTickerCode("NYSE");
        eo.setName("New York Stock Exchange");
        eo.setStocks(new ArrayList<>());
        eo = eoDao.addExchangeOrganization(eo); // Call the dao method

        ExchangeOrganization eo2 = new ExchangeOrganization();
        eo2.setTickerCode("NASDAQ");
        eo2.setName("NASDAQ Stock Market");
        eo2.setStocks(new ArrayList<>());
        eo2 = eoDao.addExchangeOrganization(eo2); // Call the dao method

        ExchangeOrganization eo3 = new ExchangeOrganization();
        eo3.setTickerCode("LSE");
        eo3.setName("London Stock Exchange");
        eo3.setStocks(new ArrayList<>());
        eo3 = eoDao.addExchangeOrganization(eo3); // Call the dao method

        //Retrieve the eo using the get dao method
        List<ExchangeOrganization> eoListFromDao = eoDao.getAllExchangeOrganizations();

        //Assert there are 3 eos in dao
        assertEquals(3, eoListFromDao.size(),"There should be 3 eos in dao");
        assertTrue(eoListFromDao.contains(eo));
        assertTrue(eoListFromDao.contains(eo2));
        assertTrue(eoListFromDao.contains(eo3));

    }

    @Test
    @DisplayName("Update Exchange Organization")
    void testUpdateExchangeOrganization() {
        ExchangeOrganization eo = new ExchangeOrganization();
        eo.setTickerCode("NASDAQ");
        eo.setName("NASDAQ Stock Market");
        eo.setStocks(new ArrayList<>());
        eo = eoDao.addExchangeOrganization(eo); // Call the dao method

        //Retrieve the eo using the get dao method
        ExchangeOrganization eoFromDao = eoDao.getExchangeOrganizationByID(eo.getExchangeOrganizationID());

        //Assert both are equal
        assertEquals(eo, eoFromDao);

        //Now update
        eo.setName("NASDAQ Updated");
        eoDao.updateExchangeOrganization(eo);

        //Should not be equal
        assertNotEquals(eo, eoFromDao);

        //Retrieve the updated exchange org
        eoFromDao =eoDao.getExchangeOrganizationByID(eo.getExchangeOrganizationID());

        //Now they should be equal
        assertEquals(eo, eoFromDao);

    }

    @Test
    @DisplayName("Delete Exchange Orgnaization By ID")
    void testDeleteExchangeOrganizationByID() {
        ExchangeOrganization eo = new ExchangeOrganization();
        eo.setTickerCode("NASDAQ");
        eo.setName("NASDAQ Stock Market");
        eo.setStocks(new ArrayList<>());
        eo = eoDao.addExchangeOrganization(eo); // Call the dao method

        //Retrieve the eo using the get dao method
        ExchangeOrganization eoFromDao = eoDao.getExchangeOrganizationByID(eo.getExchangeOrganizationID());

        assertNotNull(eoFromDao);

        //Now delete
        eoDao.deleteExchangeOrganizationByID(eo.getExchangeOrganizationID());

        //Try to retrieve deleted object from dao
        eoFromDao =eoDao.getExchangeOrganizationByID(eo.getExchangeOrganizationID());

        //But it should be null
        assertNull(eoFromDao);
    }

    @Test
    @DisplayName("Get Exchange Organization By Stock")
    void testGetExchangeOrganizationsByStock() {
        // Create two companies and their respective stocks
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
        stock2.setCompany(company);
        stock2.setExchangeOrganizations(new ArrayList<>());
        stock2 = stockDao.addStock(stock2); // add the stock to the dao

        List<Stock> stockList1 = new ArrayList<>();
        stockList1.add(stock);
        stockList1.add(stock2);

        List<Stock> stockList2 = new ArrayList<>();
        stockList2.add(stock);

        ExchangeOrganization eo = new ExchangeOrganization();
        eo.setTickerCode("NYSE");
        eo.setName("New York Stock Exchange");
        eo.setStocks(stockList1);
        eo = eoDao.addExchangeOrganization(eo); // Call the dao method

        ExchangeOrganization eo2 = new ExchangeOrganization();
        eo2.setTickerCode("NASDAQ");
        eo2.setName("NASDAQ Stock Market");
        eo2.setStocks(stockList2);
        eo2 = eoDao.addExchangeOrganization(eo2); // Call the dao method


        List<ExchangeOrganization> eoList1 = eoDao.getExchangeOrganizationsByStock(stock);
        List<ExchangeOrganization> eoList2 = eoDao.getExchangeOrganizationsByStock(stock2);


        assertEquals(2, eoList1.size(), "The first organization should have 2 stocks");
        assertEquals(1, eoList2.size(), "The second organization should have 1 stock");

    }
}