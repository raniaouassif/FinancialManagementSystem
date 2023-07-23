package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.ExchangeOrganization;
import com.sg.FinancialManagementSystem.dto.Stock;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
public interface ExchangeOrganizationDao {

    ExchangeOrganization getExchangeOrganizationByID(int exchangeOrganizationID);

    List<ExchangeOrganization> getAllExchangeOrganizations();

    ExchangeOrganization addExchangeOrganization(ExchangeOrganization exchangeOrganization);

    void updateExchangeOrganization(ExchangeOrganization exchangeOrganization);

    void deleteExchangeOrganizationByID(int exchangeOrganizationID);

    List<ExchangeOrganization> getExchangeOrganizationsByStock(Stock stock);
}
