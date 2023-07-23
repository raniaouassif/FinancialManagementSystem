package com.sg.FinancialManagementSystem.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
public class Portfolio {
    private int portfolioID;
    private BigDecimal balance;
    private List<Stock> stocks;

}
