package com.sg.FinancialManagementSystem.dto;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
public class ExchangeOrganization {
    private int exchangeOrganizationID;
    private String tickerCode;
    private String name;
    private List<Stock> stocks;
}
