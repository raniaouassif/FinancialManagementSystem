package com.sg.FinancialManagementSystem.service;

import com.sg.FinancialManagementSystem.dao.CompanyDao;
import com.sg.FinancialManagementSystem.dto.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-26
 */

@Service
public class CompanyServiceImpl implements CompanyServiceInterface{
    @Autowired
    CompanyDao companyDao;
    @Override
    public Company getCompanyByID(int companyID) {
        return companyDao.getCompanyByID(companyID);
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyDao.getAllCompanies();
    }

    @Override
    public Company addCompany(Company company) {
        return companyDao.addCompany(company);
    }

    @Override
    public void updateCompany(Company company) {
        companyDao.updateCompany(company);
    }

    @Override
    public void deleteCompanyByID(int companyID) {
        companyDao.deleteCompanyByID(companyID);
    }

    @Override
    public List<Company> getPublicCompanies() {
        return companyDao.getPublicCompanies();
    }

    @Override
    public List<Company> getPrivateCompanies() {
        return companyDao.getPrivateCompanies();
    }
}
