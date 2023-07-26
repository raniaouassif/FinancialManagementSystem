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

            final String GET_COMPANY_BY_ID = "SELECT * FROM Company WHERE companyID = ?";
            Company company =  jdbcTemplate.queryForObject(GET_COMPANY_BY_ID, new CompanyMapper(), companyID);
            company.setStock(getStockByCompany(companyID));
            return company;

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
        final String ADD_COMPANY = "INSERT INTO Company " +
                "(name, industry, status, revenue, profit, grossMargin, cashFlow) " +
                "VALUES (?,?,?,?,?,?,?);";

        jdbcTemplate.update(ADD_COMPANY,
                company.getName(),
                company.getIndustry(),
                company.getStatus().toString(), // A company starts as private
                company.getRevenue(),
                company.getProfit(),
                company.getGrossMargin(),
                company.getCashFlow()
        );

        int newID = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        company.setCompanyID(newID);

        return company;
    }

    @Override
    public void updateCompany(Company company) {
        final String UPDATE_COMPANY = "UPDATE Company SET " +
                "name = ?, industry = ?, status = ?, " +
                "revenue = ?, profit = ?, grossMargin = ?, cashFlow = ? " +
                "WHERE companyID = ?";

        jdbcTemplate.update(UPDATE_COMPANY,
                company.getName(),
                company.getIndustry(),
                company.getStatus().toString(),
                company.getRevenue(),
                company.getProfit(),
                company.getGrossMargin(),
                company.getCashFlow());
    }

    @Override
    public void deleteCompanyByID(int companyID) {
        //First delete from the Portfolio Bridge
        final String DELETE_PORTFOLIO_BRIDGE = "DELETE pb FROM PortfolioBridge pb " +
                "JOIN StockExchangeOrganization seo ON seo.stockID = pb.stockID AND seo.exchangeOrganizationID = pb.exchangeOrganizationID " +
                "JOIN Stock s ON seo.stockID = s.stockID " +
                "JOIN Company c ON c.companyID = s.companyID " +
                "WHERE c.companyID = ?";
        jdbcTemplate.update(DELETE_PORTFOLIO_BRIDGE, companyID);

        //Then delete from the PortfolioStock bridge
        final String DELETE_PORTFOLIO_STOCK = "DELETE ps FROM PortfolioStock ps " +
                "JOIN Stock s ON s.stockID = ps.stockID " +
                "JOIN Company c ON c.companyID = s.companyID " +
                "WHERE c.companyID = ?";
        jdbcTemplate.update(DELETE_PORTFOLIO_STOCK, companyID);

        //Then delete the Stock
        final String DELETE_COMPANY_STOCK = "DELETE FROM Stock WHERE companyID = ?";
        jdbcTemplate.update(DELETE_COMPANY_STOCK, companyID);

        //Finally delete the company
        final String DELETE_COMPANY = "DELETE FROM Company WHERE companyID = ?";
        jdbcTemplate.update(DELETE_COMPANY, companyID);
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
        try {
            final String GET_STOCK_BY_COMPANY = "SELECT * FROM Stock WHERE companyID = ?";
            //todo set ?
            return jdbcTemplate.queryForObject(GET_STOCK_BY_COMPANY, new StockMapper(), companyID);
        } catch (DataAccessException e) {
            return null;
        }
    }

    private void setStockByCompanyList(List<Company> companyList) {
        for(Company company : companyList) {
            company.setStock(getStockByCompany(company.getCompanyID()));
        }
    }
}
