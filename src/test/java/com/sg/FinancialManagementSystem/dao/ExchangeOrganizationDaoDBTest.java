package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.ExchangeOrganization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author raniaouassif on 2023-07-25
 */
@SpringBootTest
class ExchangeOrganizationDaoDBTest {

    @Autowired
    ExchangeOrganizationDao eoDao;
    @BeforeEach
    void setUp() {
        //Delete all exchange organizations
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
    void getAllExchangeOrganizations() {
    }

    @Test
    void addExchangeOrganization() {
    }

    @Test
    void updateExchangeOrganization() {
    }

    @Test
    void deleteExchangeOrganizationByID() {
    }

    @Test
    void getExchangeOrganizationsByStock() {
    }
}