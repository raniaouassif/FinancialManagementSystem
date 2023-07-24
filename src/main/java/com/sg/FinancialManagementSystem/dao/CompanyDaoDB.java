package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dao.mappers.CompanyMapper;
import com.sg.FinancialManagementSystem.dto.Company;
import com.sg.FinancialManagementSystem.dto.CompanyStatus;
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

            return jdbcTemplate.queryForObject(GET_COMPANY_BY_ID, new CompanyMapper(), companyID);
        } catch (DataAccessException e) {
            System.out.println("CompanyDaoDB: getCompanyByID() failed.");
            return null;
        }
    }

    @Override
    public List<Company> getAllCompanies() {
        final String GET_ALL_COMPANIES = "SELECT * FROM Company;";
        return jdbcTemplate.query(GET_ALL_COMPANIES, new CompanyMapper());
    }

    @Override
    public Company addCompany(Company company) {
        final String ADD_COMPANY = "INSERT INTO Company (name, industry, status) VALUES (?,?,?);";

        jdbcTemplate.update(ADD_COMPANY,
                company.getName(),
                company.getIndustry(),
                CompanyStatus.PRIVATE);

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
}
