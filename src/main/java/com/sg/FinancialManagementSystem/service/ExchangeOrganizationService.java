package com.sg.FinancialManagementSystem.service;

import com.sg.FinancialManagementSystem.dto.ExchangeOrganization;
import com.sg.FinancialManagementSystem.dto.Stock;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-27
 */
public interface ExchangeOrganizationService {
    ExchangeOrganization getExchangeOrganizationByID(int exchangeOrganizationID);

    List<ExchangeOrganization> getAllExchangeOrganizations();

    ExchangeOrganization addExchangeOrganization(ExchangeOrganization exchangeOrganization);

    void updateExchangeOrganization(ExchangeOrganization exchangeOrganization);

    void deleteExchangeOrganizationByID(int exchangeOrganizationID);

    List<ExchangeOrganization> getExchangeOrganizationsByStock(Stock stock);
}
