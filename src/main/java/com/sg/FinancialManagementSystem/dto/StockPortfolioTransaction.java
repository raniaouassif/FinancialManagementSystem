package com.sg.FinancialManagementSystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author raniaouassif on 2023-07-23
 */
public class StockPortfolioTransaction {
    private int stockPortfolioTransactionID;
    private LocalDateTime dateTime;
    private int numberOfShares;
    private BigDecimal transactionCost;
}
