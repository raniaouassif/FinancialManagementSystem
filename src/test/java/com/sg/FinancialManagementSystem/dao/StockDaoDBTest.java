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
class StockDaoDBTest {

    @Autowired
    StockDao stockDao;

    @Autowired
    CompanyDao companyDao;

    @Autowired
    ExchangeOrganizationDao eoDao;

    @BeforeEach
    void setUp() {
        //Delete all exchange organizations
        List<ExchangeOrganization> exchangeOrganizationList = eoDao.getAllExchangeOrganizations();
        for(ExchangeOrganization eo : exchangeOrganizationList) {
            eoDao.deleteExchangeOrganizationByID(eo.getExchangeOrganizationID());
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


    }

    @Test
    @DisplayName("Get And Add Stock")
    void testGetAndAddStock() {
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

        //Get the stock from the dao
        Stock stockFromDao = stockDao.getStockByID(stock.getStockID());

        //Assert they are equal
        assertEquals(stock, stockFromDao);

    }

    @Test
    @DisplayName("Get All Stocks")
    void testGetAllStocks() {
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
        stock2.setCompany(company2);
        stock2.setExchangeOrganizations(new ArrayList<>());
        stock2 = stockDao.addStock(stock2); // add the stock to the dao

        //Get ALL the stock from the dao
        List<Stock> stockFromDao = stockDao.getAllStocks();

        //Assert they are equal
        assertEquals(2, stockFromDao.size());
    }



    @Test
    @DisplayName("Update Stock")
    void testUpdateStock() {
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

        //Get the stock from the dao
        Stock stockFromDao = stockDao.getStockByID(stock.getStockID());

        //Assert they are equal
        assertEquals(stock, stockFromDao);

        // Now update the stock
        long updatedNumberOfOutstandingShares = 1500L;
        BigDecimal updatedSharePrice = new BigDecimal("2500.03").setScale(2, RoundingMode.HALF_UP);

        // Calculate market capitalization
        BigDecimal updatedMarketCap = updatedSharePrice.multiply(BigDecimal.valueOf(updatedNumberOfOutstandingShares)).setScale(2, RoundingMode.HALF_UP);

        // Set the updated market cap
        stock.setMarketCap(updatedMarketCap);

        //  New daily volume
        stock.setDailyVolume(5000L);

        //Call the DAO method update
        stockDao.updateStock(stock);

        // The updated stock should not be equal to the retrieved dao yet
        assertNotEquals(stock, stockFromDao);

        // Now retrieve the updated stock
        stockFromDao = stockDao.getStockByID(stock.getStockID());

        // The objects should now be equal
        assertEquals(stock, stockFromDao);
    }

    @Test
    @DisplayName("Delete Stock By ID ")
    void testDeleteStockByID() {
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

        //Get the stock from the dao
        Stock stockFromDao = stockDao.getStockByID(stock.getStockID());

        //Assert they are equal
        assertEquals(stock, stockFromDao);

        //Now delete the stock
        stockDao.deleteStockByID(stock.getStockID());

        //Try to retrieve the object
        stockFromDao = stockDao.getStockByID(stock.getStockID());

        // The stock from dao should be null
        assertNull(stockFromDao);
    }

    @Test
    @DisplayName("Get Stock By Company")
    void testGetStockByCompany() {
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
        stock2.setCompany(company2);
        stock2.setExchangeOrganizations(new ArrayList<>());
        stock2 = stockDao.addStock(stock2); // add the stock to the dao

        //Get stock from the dao for company AMAZON
        Stock stockFromDao = stockDao.getStockByCompany(company);

        assertEquals(stockFromDao, stock);

    }

    @Test
    @DisplayName("Get Stocks By Exchange Organization")
    void testGetStocksByEo() {

        //Create thr first Exchange Organizations
        ExchangeOrganization nasdaqEO = new ExchangeOrganization();
        nasdaqEO.setTickerCode("NASDAQ");
        nasdaqEO.setName("NASDAQ Stock Market");
        nasdaqEO.setStocks(new ArrayList<>());
        nasdaqEO = eoDao.addExchangeOrganization(nasdaqEO); // Call the dao method

        List<ExchangeOrganization> nasdaqEOList = new ArrayList<>();
        nasdaqEOList.add(nasdaqEO);

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
        stock.setExchangeOrganizations(nasdaqEOList);
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
        stock2.setExchangeOrganizations(nasdaqEOList);
        stock2 = stockDao.addStock(stock2); // add the stock to the dao

        // Create the third company and its respective stock
        Company company3 = new Company();
        company3.setName("Google");
        company3.setIndustry("Technology and Internet Services");
        company3.setStatus(CompanyStatus.PUBLIC);
        company3.setRevenue(new BigDecimal("789.512").setScale(3, RoundingMode.HALF_UP));
        company3.setProfit(new BigDecimal("345.876").setScale(3, RoundingMode.HALF_UP));
        company3.setGrossMargin(new BigDecimal("38.9").setScale(3, RoundingMode.HALF_UP));
        company3.setCashFlow(new BigDecimal("56.32").setScale(3, RoundingMode.HALF_UP));
        company3 = companyDao.addCompany(company3); // add the company to the dao

        Stock stock3 = new Stock();
        stock3.setTickerCode("GOOGL");
        stock3.setStatus(StockStatus.LISTED);
        stock3.setSharePrice(new BigDecimal("2999.58").setScale(2, RoundingMode.HALF_UP));
        stock3.setNumberOfOutstandingShares(1800000000L);
        stock3.setMarketCap(new BigDecimal(2922320000000L).setScale(2, RoundingMode.HALF_UP));
        stock3.setDailyVolume(800000L);
        stock3.setCompany(company3);
        stock3.setExchangeOrganizations(new ArrayList<>()); // NOT TRADED IN NASDAQ
        stock3 = stockDao.addStock(stock3); // add the stock to the dao

        // GET ALL STOCKS FROM NASDAQ:
        List<Stock> nasdaqStocksFromDao = stockDao.getStocksByEo(nasdaqEO);

        assertEquals(2, nasdaqStocksFromDao.size());

    }

    @Test
    void getStocksByPortfolio() {
        //TODO
    }
}