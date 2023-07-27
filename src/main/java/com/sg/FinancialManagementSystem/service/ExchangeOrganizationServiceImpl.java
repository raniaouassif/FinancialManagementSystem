package com.sg.FinancialManagementSystem.service;

import com.sg.FinancialManagementSystem.dao.ExchangeOrganizationDao;
import com.sg.FinancialManagementSystem.dto.ExchangeOrganization;
import com.sg.FinancialManagementSystem.dto.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-27
 */
@Service
public class ExchangeOrganizationServiceImpl implements ExchangeOrganizationService{
    @Autowired
    ExchangeOrganizationDao exchangeOrganizationDao;
    @Override
    public ExchangeOrganization getExchangeOrganizationByID(int exchangeOrganizationID) {
        return exchangeOrganizationDao.getExchangeOrganizationByID(exchangeOrganizationID);
    }

    @Override
    public List<ExchangeOrganization> getAllExchangeOrganizations() {
        return exchangeOrganizationDao.getAllExchangeOrganizations();
    }

    @Override
    public ExchangeOrganization addExchangeOrganization(ExchangeOrganization exchangeOrganization) {
        return exchangeOrganizationDao.addExchangeOrganization(exchangeOrganization);
    }

    @Override
    public void updateExchangeOrganization(ExchangeOrganization exchangeOrganization) {
        exchangeOrganizationDao.updateExchangeOrganization(exchangeOrganization);
    }

    @Override
    public void deleteExchangeOrganizationByID(int exchangeOrganizationID) {
        exchangeOrganizationDao.deleteExchangeOrganizationByID(exchangeOrganizationID);
    }

    @Override
    public List<ExchangeOrganization> getExchangeOrganizationsByStock(Stock stock) {
        return exchangeOrganizationDao.getExchangeOrganizationsByStock(stock);
    }
}
