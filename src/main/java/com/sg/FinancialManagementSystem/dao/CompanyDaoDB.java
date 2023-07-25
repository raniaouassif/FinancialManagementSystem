package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dao.mappers.CompanyMapper;
import com.sg.FinancialManagementSystem.dao.mappers.StockMapper;
import com.sg.FinancialManagementSystem.dto.Company;
import com.sg.FinancialManagementSystem.dto.CompanyStatus;
import com.sg.FinancialManagementSystem.dto.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-24
 */
@Repository
public class CompanyDaoDB implements CompanyDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public Company getCompanyByID(int companyID) {
        try {
            final String GET_COMPANY_BY_ID = "SELECT * FROM Company WHERE companyID = ?";
            Company company =  jdbcTemplate.queryForObject(GET_COMPANY_BY_ID, new CompanyMapper(), companyID);
            company.setStock(getStockByCompany(companyID));
            return company;
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Company> getAllCompanies() {
        final String GET_ALL_COMPANIES = "SELECT * FROM Company;";
        List<Company> companyList =  jdbcTemplate.query(GET_ALL_COMPANIES, new CompanyMapper());
        setStockByCompanyList(companyList);
        return companyList;
    }

    @Override
    public Company addCompany(Company company) {
        final String ADD_COMPANY = "INSERT INTO Company (name, industry, status) VALUES (?,?,?);";

        jdbcTemplate.update(ADD_COMPANY,
                company.getName(),
                company.getIndustry(),
                CompanyStatus.PRIVATE.toString()); // A company starts as private

        int newID = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        company.setCompanyID(newID);

        return company;
    }

    @Override
    public void updateCompany(Company company) {
        final String UPDATE_COMPANY = "UPDATE Company SET name = ?, industry = ?, status = ? WHERE companyID = ?";

        jdbcTemplate.update(UPDATE_COMPANY,
                company.getName(),
                company.getIndustry(),
                company.getStatus());
    }

    @Override
    public void deleteCompanyByID(int companyID) {
        //todo
    }

    @Override
    public List<Company> getPublicCompanies() {
        final String GET_PUBLIC_COMPANIES = "SELECT * FROM Company WHERE status='PUBLIC';";
        List<Company> companyList =  jdbcTemplate.query(GET_PUBLIC_COMPANIES, new CompanyMapper());
        setStockByCompanyList(companyList);
        return companyList;
    }

    @Override
    public List<Company> getPrivateCompanies() {
        final String GET_PRIVATE_COMPANIES = "SELECT * FROM Company WHERE status='PRIVATE';";
        List<Company> companyList =  jdbcTemplate.query(GET_PRIVATE_COMPANIES, new CompanyMapper());
        setStockByCompanyList(companyList);
        return companyList;
    }

    //PRIVATE HELPER FUNCTIONS
    private Stock getStockByCompany(int companyID) {
        final String GET_STOCK_BY_COMPANY = "SELECT * FROM Stock WHERE companyID = ?";
        //todo set ?
        return jdbcTemplate.queryForObject(GET_STOCK_BY_COMPANY, new StockMapper(), companyID);
    }

    private void setStockByCompanyList(List<Company> companyList) {
        for(Company company : companyList) {
            company.setStock(getStockByCompany(company.getCompanyID()));
        }
    }
}
