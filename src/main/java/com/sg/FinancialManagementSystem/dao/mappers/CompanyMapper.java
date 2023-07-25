package com.sg.FinancialManagementSystem.dao.mappers;

import com.sg.FinancialManagementSystem.dto.Company;
import com.sg.FinancialManagementSystem.dto.CompanyStatus;
import jdk.jshell.Snippet;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author raniaouassif on 2023-07-23
 */
public class CompanyMapper implements RowMapper<Company> {

    @Override
    public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
        Company company = new Company();
        company.setCompanyID(rs.getInt("companyID"));
        company.setName(rs.getString("name"));
        company.setIndustry(rs.getString("industry"));
        company.setStatus(CompanyStatus.valueOf(rs.getString("status")));
        company.setRevenue(rs.getBigDecimal("revenue"));
        company.setProfit(rs.getBigDecimal("profit"));
        company.setGrossMargin(rs.getBigDecimal("grossMargin"));
        company.setCashFlow(rs.getBigDecimal("cashFlow"));

        return company;
    }
}
