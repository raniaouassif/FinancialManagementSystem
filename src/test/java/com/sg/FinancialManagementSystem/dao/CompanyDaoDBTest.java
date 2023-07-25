package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.Company;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author raniaouassif on 2023-07-25
 */
@SpringBootTest
class CompanyDaoDBTest {
    @Autowired
    CompanyDao companyDao;
    @BeforeEach
    void setUp() {
        //Delete Companies
    }

    @Test
    @DisplayName("Add And Get Company")
    void getCompanyByID() {
        Company company = new Company();
        company.setName("Amazon");
        company.setIndustry("Multinational Technology Company");
        // Company should be set to private at first
        company.setRevenue(new BigDecimal(514.22).setScale(3, RoundingMode.HALF_UP));
        company.setProfit(new BigDecimal(225.152).setScale(3, RoundingMode.HALF_UP));
        company.setGrossMargin(new BigDecimal(41.8).setScale(3, RoundingMode.HALF_UP));
        company.setCashFlow(new BigDecimal(25.18).setScale(3, RoundingMode.HALF_UP));
        company = companyDao.addCompany(company); // add the company to the dao

        Company companyFromDao = companyDao.getCompanyByID(company.getCompanyID());

        assertEquals(company, companyFromDao);


    }

    @Test
    void getAllCompanies() {
    }

    @Test
    void addCompany() {
    }

    @Test
    void updateCompany() {
    }

    @Test
    void deleteCompanyByID() {
    }

    @Test
    void getPublicCompanies() {
    }

    @Test
    void getPrivateCompanies() {
    }
}