package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.Company;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
public interface CompanyDao {
    Company getCompanyByID(int companyID);

    List<Company> getAllCompanies();

    Company addCompany(Company company);

    void updateCompany(Company company);

    void deleteCompanyByID(int companyID);

    List<Company> getPublicCompanies();

    List<Company> getPrivateCompanies();


}
