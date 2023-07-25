package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.Company;
import com.sg.FinancialManagementSystem.dto.CompanyStatus;
import com.sg.FinancialManagementSystem.dto.Stock;
import com.sg.FinancialManagementSystem.dto.StockStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

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
    void getAllStocks() {
    }

    @Test
    void addStock() {
    }

    @Test
    void updateStock() {
    }

    @Test
    void deleteStockByID() {
    }

    @Test
    void getStockByCompany() {
    }

    @Test
    void getStocksByEo() {
    }

    @Test
    void getStocksByPortfolio() {
    }
}