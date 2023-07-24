package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.ExchangeOrganization;
import com.sg.FinancialManagementSystem.dto.Stock;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
public class ExchangeOrganizationDB implements ExchangeOrganizationDao{
    @Override
    public ExchangeOrganization getExchangeOrganizationByID(int exchangeOrganizationID) {
        return null;
    }

    @Override
    public List<ExchangeOrganization> getAllExchangeOrganizations() {
        return null;
    }

    @Override
    public ExchangeOrganization addExchangeOrganization(ExchangeOrganization exchangeOrganization) {
        return null;
    }

    @Override
    public void updateExchangeOrganization(ExchangeOrganization exchangeOrganization) {

    }

    @Override
    public void deleteExchangeOrganizationByID(int exchangeOrganizationID) {

    }

    @Override
    public List<ExchangeOrganization> getExchangeOrganizationsByStock(Stock stock) {
        return null;
    }
}
